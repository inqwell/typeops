# Introduction to typeops

Alternative type outcomes for arithmetic in Clojure.

[![Clojars Project](http://clojars.org/typeops/latest-version.svg)](http://clojars.org/typeops)

`[typeops "0.1.2"]`

* In Clojure, functions are agnostic about argument types, yet the host platform is not and likely
neither is your database.

* If you are writing a calculation engine then in many cases floating point
types are unsuitable - inaccuracies will accumulate making results unpredictable.

* Value type systems and how the types combine are a policy decision. While
Clojure supports integer and floating point types, and mostly makes precise
decimal usage transparent, the way these combine as operands in the
arithmetic functions may not be to your liking.

OK:
```clojure
(/ 123.456M 3)
=> 41.152M
```

Not OK:
```clojure
(/ 123.457M 3)
ArithmeticException Non-terminating decimal expansion;
no exact representable decimal result.
java.math.BigDecimal.divide (BigDecimal.java:1690)
```

We can get round this using `with-precision` :
```clojure
(with-precision 5
  (/ 123.457M 3))
=> 41.152M
```
but this taints the code, forcing us to be aware of the underlying types,
and what precision do we choose if our interest is accuracy?

If you are using `decimal` it doesn't make sense to allow these to combine
with floating point:
```clojure
(* 123.457M 3.142)
=> 387.90189399999997
```
If you want to avoid `ratio` preferring integer arithmetic, again, you have to be
explicit:
```clojure
(/ 4 3)
=> 4/3

(quot 4 3)
=> 1
```
Typeops does the following for `+` `-` `*` and `/` :

* Integer arithmetic gives a (truncated) integer result

* Intermediate results do not lose accuracy

* decimals cannot combine with floating point

## "assign"
If you are modelling domain types it is useful to "assign" fields according to
their underlying type and accuracy, rather than say relying on your database
to do this for you:
```clojure
(def m
{:Integer  0,
 :Decimal2 0.00M,
 :Short    0,
 :Decimal  0E-15M,
 :Float    0.0,
 :Long     1,
 :Byte     0,
 :Double   0.0,
 :String   ""})

(assoc m :Decimal2 2.7182818M)
=> {:Integer 0,
    :Decimal2 2.72M,
    :Short 0,
    :Decimal 0E-15M,
    :Float 0.0,
    :Long 1,
    :Byte 0,
    :Double 0.0,
    :String ""}
```
### nil
If your domain model permits `NULL` values you can represent these as `nil` in
Clojure. This destroys the type information however if a map has meta data:
```clojure
(meta m)
=> {:proto {:Integer 0, :Decimal2 0.00M, ...}}
```
then "assigning" away from `nil` will use the corresponding field in
the `:proto` map to align the type.

### Non-numerics
For non-numeric fields typeops will use any :proto to keep map values as their intended
type. Attempting to "assign" something that is false for `instance?` results in an
exception

### Error Handling
Typeops uses dynamic vars to help with error handling and debugging. Bind `*debug*`
to `true` to carry information about type-incompatible `assoc` operations out via
the exception.
```clojure
(binding [typeops.core/*debug* true]
     (assoc m :Integer "foo"))

=> ExceptionInfo Incompatible type for operation: class java.lang.String  clojure.core/ex-info (core.clj:4617)
*e

=> #error{:cause "Incompatible type for operation: class java.lang.String",
          :data {:map {:Integer 0,
                       :Decimal2 0.00M,
                       :Short 0,
                       :Decimal 0E-15M,
                       :Float 0.0,
                       :Date #inst"2019-03-28T17:14:27.816-00:00",
                       :Long 1,
                       :Byte 0,
                       :Double 0.0,
                       :String ""},
                 :key :Integer,
                 :val "foo",
                 :cur 0},
          :via [{:type clojure.lang.ExceptionInfo,
                 :message "Incompatible type for operation: class java.lang.String"
                   .
                   .
```

Bind `*warn-on-absent-key*` to a function of two arguments `(fn [m k] ...)` which will
be called when `assoc` puts a key into a map that wasn't there before.
```
(binding [typeops.assign/*warn-on-absent-key*
          (fn [m k]
            (println k "Boo!"))]
  (assoc m :Absent "foo"))
:Absent Boo!
=> {:Absent "foo",
    :Integer 0,
    :Decimal2 0.00M,
    :Short 0,
    :Decimal 0E-15M,
    :Float 0.0,
    :Date #inst"2019-03-28T17:14:27.816-00:00",
    :Long 1,
    :Byte 0,
    :Double 0.0,
    :String ""}
```

## Usage

### Per Namespace
```clojure
(ns myns
  (:refer-clojure :exclude [+ - * / assoc merge])
  (:require [typeops.core :refer :all])
  (:require [typeops.assign :refer :all]))

(+ 3.142M 2.7182818M)
=> 5.8602818M

(- 3.142M 2.7182818M 3.142M)
=> -2.7182818M

(* 3.142M 2.7182818M 3.142M)
=> 26.8353237278152M

(/ 3.142M 2.7182818M 0.1234M)
=> 9.368M

(assoc my-map k v ... ks vs)  ; assoc preserves type and precision
(assign my-map k v ... ks vs) ; same as above

```

### Globally
Call the function `init-global!` somewhere in your system start up to
alter the vars `+` `-` `*` and `/` in `clojure.core`.

## License

Copyright © 2018-2019 Inqwell Ltd

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
