# typeops

Alternative type outcomes for arithmetic in Clojure.

[![Clojars Project](http://clojars.org/typeops/latest-version.svg)](http://clojars.org/typeops)

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
If you want to avoid `ratio` preferring integer arithmetic you have to be
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

(assign m :Decimal2 2.7182818M)
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

## Usage

### Per Namespace
```clojure
(ns myns
  (:refer-clojure :exclude [+ - * /])
  (:require [typeops.core :refer :all]))

(+ 3.142M 2.7182818M)
=> 5.8602818M

(- 3.142M 2.7182818M 3.142M)
=> -2.7182818M

(* 3.142M 2.7182818M 3.142M)
=> 26.8353237278152M

(/ 3.142M 2.7182818M 0.1234M)
=> 9.368M

(assign my-map k v ... ks vs)
```

### Globally
Call the function `init-global!` somewhere in your system start up to
alter the vars `+` `-` `*` and `/` in `clojure.core`.

## License

Copyright © 2017 Inqwell Ltd

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
