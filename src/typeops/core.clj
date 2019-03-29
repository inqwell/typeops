(ns typeops.core
  (:refer-clojure :exclude [+ - * /])
  (:import (java.math BigDecimal)))

(def ^:no-doc core-divide clojure.core//)
(def ^:no-doc core-multiply clojure.core/*)
(def ^:no-doc core-add clojure.core/+)
(def ^:no-doc core-subtract clojure.core/-)

; TODO use math context to limit scale of multiplications?

(def ^:private float-ex "Can't combine arbitrary precision with floating point")

(def ^:dynamic *rounding*
  "The rounding mode applied when decimal accuracy is lost. Defaults
  to BigDecimal/ROUND_HALF_UP"
  BigDecimal/ROUND_HALF_UP)

(def ^:dynamic *debug*
  "When set to true (not just truthy) any exception thrown
  for assoc or merge operations that cause type violations
  will carry the data {:map map :key key :val val :cur cur}"
  {})

(defn- illegal-operand-types
  [^String msg]
  (throw (ex-info msg *debug*)))

(defn- unknown-type
  [obj]
  (throw (ex-info (str "Unknown type for operation: " (type obj)) *debug*)))

(defn- incompatible-type
  [obj]
  (throw (ex-info (str "Incompatible type for operation: " (type obj)) *debug*)))

(defprotocol ^:no-doc ITypedOp
  (op-assign [to from])
  (op-divide [dividend divisor])
  (op-multiply [multiplier multiplicand])
  (op-add [addend augend])
  (op-subtract [minuend subtrahend]))

(defprotocol ^:no-doc IToBigDecimal
  (assign-dec [from to])
  (divide-dec [divisor dividend])
  (multiply-dec [multiplicand multiplier])
  (add-dec [augend addend])
  (subtract-dec [subtrahend minuend]))

(defprotocol ^:no-doc IToDouble
  (assign-double [from to])
  (divide-double [divisor dividend])
  (multiply-double [multiplicand multiplier])
  (add-double [augend addend])
  (subtract-double [subtrahend minuend]))

(defprotocol ^:no-doc IToFloat
  (assign-float [from to])
  (divide-float [divisor dividend])
  (multiply-float [multiplicand multiplier])
  (add-float [augend addend])
  (subtract-float [subtrahend minuend]))

(defprotocol ^:no-doc IToLong
  (assign-long [from to])
  (divide-long [divisor dividend])
  (multiply-long [multiplicand multiplier])
  (add-long [augend addend])
  (subtract-long [subtrahend minuend]))

(defprotocol ^:no-doc IToInteger
  (assign-int [from to])
  (divide-int [divisor dividend])
  (multiply-int [multiplicand multiplier])
  (add-int [augend addend])
  (subtract-int [subtrahend minuend]))

(defprotocol ^:no-doc IToShort
  (assign-short [from to])
  (divide-short [divisor dividend])
  (multiply-short [multiplicand multiplier])
  (add-short [augend addend])
  (subtract-short [subtrahend minuend]))

(defprotocol ^:no-doc IToByte
  (assign-byte [from to])
  (divide-byte [divisor dividend])
  (multiply-byte [multiplicand multiplier])
  (add-byte [augend addend])
  (subtract-byte [subtrahend minuend]))

(extend-protocol ITypedOp
  BigDecimal
  (op-assign [to from]
    (assign-dec from to))
  (op-divide [dividend divisor]
    (divide-dec divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-dec multiplicand multiplier))
  (op-add [addend augend]
    (add-dec augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-dec subtrahend minuend))

  Double
  (op-assign [to from]
    (assign-double from to))
  (op-divide [dividend divisor]
    (divide-double divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-double multiplicand multiplier))
  (op-add [addend augend]
    (add-double augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-double subtrahend minuend))

  Float
  (op-assign [to from]
    (assign-float from to))
  (op-divide [dividend divisor]
    (divide-float divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-float multiplicand multiplier))
  (op-add [addend augend]
    (add-float augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-float subtrahend minuend))

  Long
  (op-assign [to from]
    (assign-long from to))
  (op-divide [dividend divisor]
    (divide-long divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-long multiplicand multiplier))
  (op-add [addend augend]
    (add-long augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-long subtrahend minuend))

  Integer
  (op-assign [to from]
    (assign-int from to))
  (op-divide [dividend divisor]
    (divide-int divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-int multiplicand multiplier))
  (op-add [addend augend]
    (add-int augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-int subtrahend minuend))

  Short
  (op-assign [to from]
    (assign-short from to))
  (op-divide [dividend divisor]
    (divide-short divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-short multiplicand multiplier))
  (op-add [addend augend]
    (add-short augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-short subtrahend minuend))

  Byte
  (op-assign [to from]
    (assign-byte from to))
  (op-divide [dividend divisor]
    (divide-byte divisor dividend))
  (op-multiply [multiplier multiplicand]
    (multiply-byte multiplicand multiplier))
  (op-add [addend augend]
    (add-byte augend addend))
  (op-subtract [minuend subtrahend]
    (subtract-byte subtrahend minuend))

  ; If there is no explicit despatch (the current value is a date, say)
  ; then allow nil or anything type compatible
  Object
  (op-assign [to from]
    (if (nil? from)
      from
      (if (instance? (class to) from)
        from
        (illegal-operand-types (str (class from) " is not type compatible with " (class to))))))
  (op-divide [dividend divisor]
    (unknown-type dividend))
  (op-multiply [multiplier multiplicand]
    (unknown-type multiplier))
  (op-add [addend augend]
    (unknown-type addend))
  (op-subtract [minuend subtrahend]
    (unknown-type minuend))

  ;Assigning away from nil to something else is OK. Everything
  ;else is NPE
  nil
  (op-assign [to from]
    from)
  (op-divide [dividend divisor]
    (throw (NullPointerException.)))
  (op-multiply [multiplier multiplicand]
    (throw (NullPointerException.)))
  (op-add [addend augend]
    (throw (NullPointerException.)))
  (op-subtract [minuend subtrahend]
    (throw (NullPointerException.))))


(extend-protocol IToBigDecimal
  BigDecimal
  (assign-dec [^BigDecimal from ^BigDecimal to]
    (.setScale from (.scale to) *rounding*))
  (divide-dec [^BigDecimal divisor ^BigDecimal dividend]
    (.divide dividend divisor *rounding*))
  (multiply-dec [^BigDecimal multiplicand ^BigDecimal multiplier]
    (.multiply multiplier multiplicand))
  (add-dec [^BigDecimal augend ^BigDecimal addend]
    (.add addend augend))
  (subtract-dec [^BigDecimal subtrahend ^BigDecimal minuend]
    (.subtract minuend subtrahend))

  Double
  (assign-dec [^Double from ^BigDecimal to]
    (illegal-operand-types float-ex))
  (divide-dec [^Double divisor ^BigDecimal dividend]
    (illegal-operand-types float-ex))
  (multiply-dec [^Double multiplicand ^BigDecimal multiplier]
    (illegal-operand-types float-ex))
  (add-dec [^Double augend ^BigDecimal addend]
    (illegal-operand-types float-ex))
  (subtract-dec [^Double subtrahend ^BigDecimal minuend]
    (illegal-operand-types float-ex))

  Float
  (assign-dec [^Float from ^BigDecimal to]
    (illegal-operand-types float-ex))
  (divide-dec [^Float divisor ^BigDecimal dividend]
    (illegal-operand-types float-ex))
  (multiply-dec [^Float multiplicand ^BigDecimal multiplier]
    (illegal-operand-types float-ex))
  (add-dec [^Float augend ^BigDecimal addend]
    (illegal-operand-types float-ex))
  (subtract-dec [^Float subtrahend ^BigDecimal minuend]
    (illegal-operand-types float-ex))

  Long
  (assign-dec [^Long from ^BigDecimal to]
    (.setScale (BigDecimal. (.longValue from))
               (.scale to)
               *rounding*))
  (divide-dec [^Long divisor ^BigDecimal dividend]
    (.divide dividend
             (BigDecimal. (.longValue divisor))
             *rounding*))
  (multiply-dec [^Long multiplicand ^BigDecimal multiplier]
    (.multiply multiplier (BigDecimal. (.longValue multiplicand))))
  (add-dec [^Long augend ^BigDecimal addend]
    (.add addend (BigDecimal. (.longValue augend))))
  (subtract-dec [^Long subtrahend ^BigDecimal minuend]
    (.subtract minuend (BigDecimal. (.longValue subtrahend))))

  Integer
  (assign-dec [^Integer from ^BigDecimal to]
    (.setScale (BigDecimal. (.intValue from))
               (.scale to)
               *rounding*))
  (divide-dec [^Integer divisor ^BigDecimal dividend]
    (.divide dividend
             (BigDecimal. (.intValue divisor))
             *rounding*))
  (multiply-dec [^Integer multiplicand ^BigDecimal multiplier]
    (.multiply multiplier (BigDecimal. (.intValue multiplicand))))
  (add-dec [^Integer augend ^BigDecimal addend]
    (.add addend (BigDecimal. (.intValue augend))))
  (subtract-dec [^Integer subtrahend ^BigDecimal minuend]
    (.subtract minuend (BigDecimal. (.intValue subtrahend))))

  Short
  (assign-dec [^Short from ^BigDecimal to]
    (.setScale (BigDecimal. (.intValue from))
               (.scale to)
               *rounding*))
  (divide-dec [^Short divisor ^BigDecimal dividend]
    (.divide dividend
             (BigDecimal. (.intValue divisor))
             *rounding*))
  (multiply-dec [^Short multiplicand ^BigDecimal multiplier]
    (.multiply multiplier (BigDecimal. (.intValue multiplicand))))
  (add-dec [^Short augend ^BigDecimal addend]
    (.add addend (BigDecimal. (.intValue augend))))
  (subtract-dec [^Short subtrahend ^BigDecimal minuend]
    (.subtract minuend (BigDecimal. (.intValue subtrahend))))

  Byte
  (assign-dec [^Byte from ^BigDecimal to]
    (.setScale (BigDecimal. (.intValue from))
               (.scale to)
               *rounding*))
  (divide-dec [^Byte divisor ^BigDecimal dividend]
    (.divide dividend
             (BigDecimal. (.intValue divisor))
             *rounding*))
  (multiply-dec [^Byte multiplicand ^BigDecimal multiplier]
    (.multiply multiplier (BigDecimal. (.intValue multiplicand))))
  (add-dec [^Byte augend ^BigDecimal addend]
    (.add addend (BigDecimal. (.intValue augend))))
  (subtract-dec [^Byte subtrahend ^BigDecimal minuend]
    (.subtract minuend (BigDecimal. (.intValue subtrahend))))

  Object
  (assign-dec [from to]
    (incompatible-type from))
  (divide-dec [divisor dividend]
    (incompatible-type dividend))
  (multiply-dec [ multiplicand multiplier]
    (incompatible-type multiplier))
  (add-dec [augend addend]
    (incompatible-type addend))
  (subtract-dec [subtrahend minuend]
    (incompatible-type minuend))

  ; It is meaningful to assign to nil, in the way of that
  ; being NULL for a DB column for example.
  nil
  (assign-dec [from to]
    from)
  (divide-dec [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-dec [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-dec [augend addend]
    (throw (NullPointerException.)))
  (subtract-dec [subtrahend minuend]
    (throw (NullPointerException.))))

(extend-protocol IToDouble
  BigDecimal
  (assign-double [^BigDecimal from ^Double to]
    (illegal-operand-types float-ex))
  (divide-double [^BigDecimal divisor ^Double dividend]
    (illegal-operand-types float-ex))
  (multiply-double [^BigDecimal multiplicand ^Double multiplier]
    (illegal-operand-types float-ex))
  (add-double [^BigDecimal augend ^Double addend]
    (illegal-operand-types float-ex))
  (subtract-double [^BigDecimal subtrahend ^Double minuend]
    (illegal-operand-types float-ex))

  Double
  (assign-double [^Double from ^Double to]
    from)
  (divide-double [^Double divisor ^Double dividend]
    (core-divide dividend divisor))
  (multiply-double [^Double multiplicand ^Double multiplier]
    (core-multiply multiplier multiplicand))
  (add-double [^Double augend ^Double addend]
    (core-add addend augend))
  (subtract-double [^Double subtrahend ^Double minuend]
    (core-subtract minuend subtrahend))

  Float
  (assign-double [^Float from ^Double to]
    (.doubleValue from))
  (divide-double [^Float divisor ^Double dividend]
    (core-divide dividend (double divisor)))
  (multiply-double [^Float multiplicand ^Double multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-double [^Float augend ^Double addend]
    (core-add addend (double augend)))
  (subtract-double [^Float subtrahend ^Double minuend]
    (core-subtract minuend (double subtrahend)))

  Long
  (assign-double [^Long from ^Double to]
    (double from))
  (divide-double [^Long divisor ^Double dividend]
    (core-divide dividend (double divisor)))
  (multiply-double [^Long multiplicand ^Double multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-double [^Long augend ^Double addend]
    (core-add addend (double augend)))
  (subtract-double [^Long subtrahend ^Double minuend]
    (core-subtract minuend (double subtrahend)))

  Integer
  (assign-double [^Integer from ^Double to]
    (double from))
  (divide-double [^Integer divisor ^Double dividend]
    (core-divide dividend (double divisor)))
  (multiply-double [^Integer multiplicand ^Double multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-double [^Integer augend ^Double addend]
    (core-add addend (double augend)))
  (subtract-double [^Integer subtrahend ^Double minuend]
    (core-subtract minuend (double subtrahend)))

  Short
  (assign-double [^Short from ^Double to]
    (double from))
  (divide-double [^Short divisor ^Double dividend]
    (core-divide dividend (double divisor)))
  (multiply-double [^Short multiplicand ^Double multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-double [^Short augend ^Double addend]
    (core-add addend (double augend)))
  (subtract-double [^Short subtrahend ^Double minuend]
    (core-subtract minuend (double subtrahend)))

  Byte
  (assign-double [^Byte from ^Double to]
    (double from))
  (divide-double [^Byte divisor ^Double dividend]
    (core-divide dividend (double divisor)))
  (multiply-double [^Byte multiplicand ^Double multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-double [^Byte augend ^Double addend]
    (core-add addend (double augend)))
  (subtract-double [^Byte subtrahend ^Double minuend]
    (core-subtract minuend (double subtrahend)))

  Object
  (assign-double [from to]
    (incompatible-type from))
  (divide-double [divisor dividend]
    (incompatible-type dividend))
  (multiply-double [ multiplicand multiplier]
    (incompatible-type multiplier))
  (add-double [augend addend]
    (incompatible-type addend))
  (subtract-double [subtrahend minuend]
    (incompatible-type minuend))

  nil
  (assign-double [from to]
    from)
  (divide-double [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-double [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-double [augend addend]
    (throw (NullPointerException.)))
  (subtract-double [subtrahend minuend]
    (throw (NullPointerException.))))


(extend-protocol IToFloat
  BigDecimal
  (assign-float [^BigDecimal from ^Float to]
    (illegal-operand-types float-ex))
  (divide-float [^BigDecimal divisor ^Float dividend]
    (illegal-operand-types float-ex))
  (multiply-float [^BigDecimal multiplicand ^Float multiplier]
    (illegal-operand-types float-ex))
  (add-float [^BigDecimal augend ^Float addend]
    (illegal-operand-types float-ex))
  (subtract-float [^BigDecimal subtrahend ^Float minuend]
    (illegal-operand-types float-ex))

  Double
  (assign-float [^Double from ^Float to]
    (float from))
  (divide-float [^Double divisor ^Float dividend]
    (core-divide (double dividend) divisor))
  (multiply-float [^Double multiplicand ^Float multiplier]
    (core-multiply (double multiplier) multiplicand))
  (add-float [^Double augend ^Float addend]
    (core-add (double addend) augend))
  (subtract-float [^Double subtrahend ^Float minuend]
    (core-subtract (double minuend) subtrahend))

  Float
  (assign-float [^Float from ^Float to]
    from)
  (divide-float [^Float divisor ^Float dividend]
    (core-divide dividend divisor))
  (multiply-float [^Float multiplicand ^Float multiplier]
    (core-multiply multiplier multiplicand))
  (add-float [^Float augend ^Float addend]
    (core-add addend augend))
  (subtract-float [^Float subtrahend ^Float minuend]
    (core-subtract minuend subtrahend))

  Long
  (assign-float [^Long from ^Float to]
    (float from))
  (divide-float [^Long divisor ^Float dividend]
    (core-divide dividend (double divisor)))
  (multiply-float [^Long multiplicand ^Float multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-float [^Long augend ^Float addend]
    (core-add addend (double augend)))
  (subtract-float [^Long subtrahend ^Float minuend]
    (core-subtract minuend (double subtrahend)))

  Integer
  (assign-float [^Integer from ^Float to]
    (float from))
  (divide-float [^Integer divisor ^Float dividend]
    (core-divide dividend (double divisor)))
  (multiply-float [^Integer multiplicand ^Float multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-float [^Integer augend ^Float addend]
    (core-add addend (double augend)))
  (subtract-float [^Integer subtrahend ^Float minuend]
    (core-subtract minuend (double subtrahend)))

  Short
  (assign-float [^Short from ^Float to]
    (float from))
  (divide-float [^Short divisor ^Float dividend]
    (core-divide dividend (double divisor)))
  (multiply-float [^Short multiplicand ^Float multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-float [^Short augend ^Float addend]
    (core-add addend (double augend)))
  (subtract-float [^Short subtrahend ^Float minuend]
    (core-subtract minuend (double subtrahend)))

  Byte
  (assign-float [^Byte from ^Float to]
    (float from))
  (divide-float [^Byte divisor ^Float dividend]
    (core-divide dividend (double divisor)))
  (multiply-float [^Byte multiplicand ^Float multiplier]
    (core-multiply multiplier (double multiplicand)))
  (add-float [^Byte augend ^Float addend]
    (core-add addend (double augend)))
  (subtract-float [^Byte subtrahend ^Float minuend]
    (core-subtract minuend (double subtrahend)))

  Object
  (assign-float [from to]
    (incompatible-type from))
  (divide-float [divisor dividend]
    (incompatible-type dividend))
  (multiply-float [ multiplicand multiplier]
    (incompatible-type multiplier))
  (add-float [augend addend]
    (incompatible-type addend))
  (subtract-float [subtrahend minuend]
    (incompatible-type minuend))

  nil
  (assign-float [from to]
    from)
  (divide-float [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-float [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-float [augend addend]
    (throw (NullPointerException.)))
  (subtract-float [subtrahend minuend]
    (throw (NullPointerException.))))


(extend-protocol IToLong
  BigDecimal
  (assign-long [^BigDecimal from ^Long to]
    (long from))
  (divide-long [^BigDecimal divisor ^Long dividend]
    (-> (BigDecimal. (long dividend))
        (.setScale (.scale divisor))
        (.divide divisor *rounding*)))
  (multiply-long [^BigDecimal multiplicand ^Long multiplier]
    (-> (BigDecimal. (long multiplier))
        (.multiply multiplicand)))
  (add-long [^BigDecimal augend ^Long addend]
    (-> (BigDecimal. (long addend))
        (.setScale (.scale augend))
        (.add augend)))
  (subtract-long [^BigDecimal subtrahend ^Long minuend]
    (-> (BigDecimal. (long minuend))
        (.setScale (.scale subtrahend))
        (.subtract subtrahend)))

  Double
  (assign-long [^Double from ^Long to]
    (long from))
  (divide-long [^Double divisor ^Long dividend]
    (core-divide (double dividend) divisor))
  (multiply-long [^Double multiplicand ^Long multiplier]
    (core-multiply (double multiplier) multiplicand))
  (add-long [^Double augend ^Long addend]
    (core-add (double addend) augend))
  (subtract-long [^Double subtrahend ^Long minuend]
    (core-subtract (double minuend) subtrahend))

  Float
  (assign-long [^Float from ^Long to]
    (long from))
  (divide-long [^Float divisor ^Long dividend]
    (core-divide (float dividend) divisor))
  (multiply-long [^Float multiplicand ^Long multiplier]
    (core-multiply (float multiplier) multiplicand))
  (add-long [^Float augend ^Long addend]
    (core-add (float addend) augend))
  (subtract-long [^Float subtrahend ^Long minuend]
    (core-subtract (float minuend) subtrahend))

  Long
  (assign-long [^Long from ^Long to]
    from)
  (divide-long [^Long divisor ^Long dividend]
    (quot dividend divisor))
  (multiply-long [^Long multiplicand ^Long multiplier]
    (core-multiply multiplier multiplicand))
  (add-long [^Long augend ^Long addend]
    (core-add addend augend))
  (subtract-long [^Long subtrahend ^Long minuend]
    (core-subtract minuend subtrahend))

  Integer
  (assign-long [^Integer from ^Long to]
    (.longValue from))
  (divide-long [^Integer divisor ^Long dividend]
    (quot dividend divisor))
  (multiply-long [^Integer multiplicand ^Long multiplier]
    (core-multiply multiplier multiplicand))
  (add-long [^Integer augend ^Long addend]
    (core-add addend augend))
  (subtract-long [^Integer subtrahend ^Long minuend]
    (core-subtract minuend subtrahend))

  Short
  (assign-long [^Short from ^Long to]
    (.longValue from))
  (divide-long [^Short divisor ^Long dividend]
    (quot dividend divisor))
  (multiply-long [^Short multiplicand ^Long multiplier]
    (core-multiply multiplier multiplicand))
  (add-long [^Short augend ^Long addend]
    (core-add addend augend))
  (subtract-long [^Short subtrahend ^Long minuend]
    (core-subtract minuend subtrahend))

  Byte
  (assign-long [^Byte from ^Long to]
    (.longValue from))
  (divide-long [^Byte divisor ^Long dividend]
    (quot dividend divisor))
  (multiply-long [^Byte multiplicand ^Long multiplier]
    (core-multiply multiplier multiplicand))
  (add-long [^Byte augend ^Long addend]
    (core-add addend augend))
  (subtract-long [^Byte subtrahend ^Long minuend]
    (core-subtract minuend subtrahend))

  Object
  (assign-long [from to]
    (incompatible-type from))
  (divide-long [divisor dividend]
    (incompatible-type divisor))
  (multiply-long [multiplicand multiplier]
    (incompatible-type multiplicand))
  (add-long [augend addend]
    (incompatible-type augend))
  (subtract-long [subtrahend minuend]
    (incompatible-type subtrahend))

  nil
  (assign-long [from to]
    from)
  (divide-long [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-long [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-long [augend addend]
    (throw (NullPointerException.)))
  (subtract-long [subtrahend minuend]
    (throw (NullPointerException.))))


(extend-protocol IToInteger
  BigDecimal
  (assign-int [^BigDecimal from ^Integer to]
    (int from))
  (divide-int [^BigDecimal divisor ^Integer dividend]
    (-> (BigDecimal. (int dividend))
        (.setScale (.scale divisor))
        (.divide divisor *rounding*)))
  (multiply-int [^BigDecimal multiplicand ^Integer multiplier]
    (-> (BigDecimal. (int multiplier))
        (.multiply multiplicand)))
  (add-int [^BigDecimal augend ^Integer addend]
    (-> (BigDecimal. (int addend))
        (.setScale (.scale augend))
        (.add augend)))
  (subtract-int [^BigDecimal subtrahend ^Integer minuend]
    (-> (BigDecimal. (int minuend))
        (.setScale (.scale subtrahend))
        (.subtract subtrahend)))

  Double
  (assign-int [^Double from ^Integer to]
    (int from))
  (divide-int [^Double divisor ^Integer dividend]
    (core-divide (double dividend) divisor))
  (multiply-int [^Double multiplicand ^Integer multiplier]
    (core-multiply (double multiplier) multiplicand))
  (add-int [^Double augend ^Integer addend]
    (core-add (double addend) augend))
  (subtract-int [^Double subtrahend ^Integer minuend]
    (core-subtract (double minuend) subtrahend))

  Float
  (assign-int [^Float from ^Integer to]
    (int from))
  (divide-int [^Float divisor ^Integer dividend]
    (core-divide (float dividend) divisor))
  (multiply-int [^Float multiplicand ^Integer multiplier]
    (core-multiply (float multiplier) multiplicand))
  (add-int [^Float augend ^Integer addend]
    (core-add (float addend) augend))
  (subtract-int [^Float subtrahend ^Integer minuend]
    (core-subtract (float minuend) subtrahend))

  Long
  (assign-int [^Long from ^Integer to]
    (int from))
  (divide-int [^Long divisor ^Integer dividend]
    (int (quot dividend divisor)))
  (multiply-int [^Long multiplicand ^Integer multiplier]
    (core-multiply multiplier multiplicand))
  (add-int [^Long augend ^Integer addend]
    (core-add addend augend))
  (subtract-int [^Long subtrahend ^Integer minuend]
    (core-subtract minuend subtrahend))

  Integer
  (assign-int [^Integer from ^Integer to]
    from)
  (divide-int [^Integer divisor ^Integer dividend]
    (int (quot dividend divisor)))
  (multiply-int [^Integer multiplicand ^Integer multiplier]
    (core-multiply multiplier multiplicand))
  (add-int [^Integer augend ^Integer addend]
    (core-add addend augend))
  (subtract-int [^Integer subtrahend ^Integer minuend]
    (core-subtract minuend subtrahend))

  Short
  (assign-int [^Short from ^Integer to]
    (int from))
  (divide-int [^Short divisor ^Integer dividend]
    (int (quot dividend divisor)))
  (multiply-int [^Short multiplicand ^Integer multiplier]
    (core-multiply multiplier multiplicand))
  (add-int [^Short augend ^Integer addend]
    (core-add addend augend))
  (subtract-int [^Short subtrahend ^Integer minuend]
    (core-subtract minuend subtrahend))

  Byte
  (assign-int [^Byte from ^Integer to]
    (int from))
  (divide-int [^Byte divisor ^Integer dividend]
    (int (quot dividend divisor)))
  (multiply-int [^Byte multiplicand ^Integer multiplier]
    (core-multiply multiplier multiplicand))
  (add-int [^Byte augend ^Integer addend]
    (core-add addend augend))
  (subtract-int [^Byte subtrahend ^Integer minuend]
    (core-subtract minuend subtrahend))

  Object
  (assign-int [from to]
    (incompatible-type from))
  (divide-int [divisor dividend]
    (incompatible-type dividend))
  (multiply-int [ multiplicand multiplier]
    (incompatible-type multiplier))
  (add-int [augend addend]
    (incompatible-type addend))
  (subtract-int [subtrahend minuend]
    (incompatible-type minuend))

  nil
  (assign-int [from to]
    from)
  (divide-int [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-int [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-int [augend addend]
    (throw (NullPointerException.)))
  (subtract-int [subtrahend minuend]
    (throw (NullPointerException.))))

(extend-protocol IToShort
  BigDecimal
  (assign-short [^BigDecimal from ^Short to]
    (short from))
  (divide-short [^BigDecimal divisor ^Short dividend]
    (-> (BigDecimal. (int dividend))
        (.setScale (.scale divisor))
        (.divide divisor *rounding*)))
  (multiply-short [^BigDecimal multiplicand ^Short multiplier]
    (-> (BigDecimal. (int multiplier))
        (.multiply multiplicand)))
  (add-short [^BigDecimal augend ^Short addend]
    (-> (BigDecimal. (int addend))
        (.setScale (.scale augend))
        (.add augend)))
  (subtract-short [^BigDecimal subtrahend ^Short minuend]
    (-> (BigDecimal. (int minuend))
        (.setScale (.scale subtrahend))
        (.subtract subtrahend)))

  Double
  (assign-short [^Double from ^Short to]
    (short from))
  (divide-short [^Double divisor ^Short dividend]
    (core-divide (double dividend) divisor))
  (multiply-short [^Double multiplicand ^Short multiplier]
    (core-multiply (double multiplier) multiplicand))
  (add-short [^Double augend ^Short addend]
    (core-add (double addend) augend))
  (subtract-short [^Double subtrahend ^Short minuend]
    (core-subtract (double minuend) subtrahend))

  Float
  (assign-short [^Float from ^Short to]
    (short from))
  (divide-short [^Float divisor ^Short dividend]
    (core-divide (float dividend) divisor))
  (multiply-short [^Float multiplicand ^Short multiplier]
    (core-multiply (float multiplier) multiplicand))
  (add-short [^Float augend ^Short addend]
    (core-add (float addend) augend))
  (subtract-short [^Float subtrahend ^Short minuend]
    (core-subtract (float minuend) subtrahend))

  Long
  (assign-short [^Long from ^Short to]
    (short from))
  (divide-short [^Long divisor ^Short dividend]
    (short (quot dividend divisor)))
  (multiply-short [^Long multiplicand ^Short multiplier]
    (core-multiply multiplier multiplicand))
  (add-short [^Long augend ^Short addend]
    (core-add addend augend))
  (subtract-short [^Long subtrahend ^Short minuend]
    (core-subtract minuend subtrahend))

  Integer
  (assign-short [^Integer from ^Short to]
    (short from))
  (divide-short [^Integer divisor ^Short dividend]
    (short (quot dividend divisor)))
  (multiply-short [^Integer multiplicand ^Short multiplier]
    (core-multiply multiplier multiplicand))
  (add-short [^Integer augend ^Short addend]
    (core-add addend augend))
  (subtract-short [^Integer subtrahend ^Short minuend]
    (core-subtract minuend subtrahend))

  Short
  (assign-short [^Short from ^Short to]
    from)
  (divide-short [^Short divisor ^Short dividend]
    (short (quot dividend divisor)))
  (multiply-short [^Short multiplicand ^Short multiplier]
    (core-multiply multiplier multiplicand))
  (add-short [^Short augend ^Short addend]
    (core-add addend augend))
  (subtract-short [^Short subtrahend ^Short minuend]
    (core-subtract minuend subtrahend))

  Byte
  (assign-short [^Byte from ^Short to]
    (short from))
  (divide-short [^Byte divisor ^Short dividend]
    (short (quot dividend divisor)))
  (multiply-short [^Byte multiplicand ^Short multiplier]
    (core-multiply multiplier multiplicand))
  (add-short [^Byte augend ^Short addend]
    (core-add addend augend))
  (subtract-short [^Byte subtrahend ^Short minuend]
    (core-subtract minuend subtrahend))

  Object
  (assign-short [from to]
    (incompatible-type from))
  (divide-short [divisor dividend]
    (incompatible-type dividend))
  (multiply-short [ multiplicand multiplier]
    (incompatible-type multiplier))
  (add-short [augend addend]
    (incompatible-type addend))
  (subtract-short [subtrahend minuend]
    (incompatible-type minuend))

  nil
  (assign-short [from to]
    from)
  (divide-short [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-short [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-short [augend addend]
    (throw (NullPointerException.)))
  (subtract-short [subtrahend minuend]
    (throw (NullPointerException.))))

(extend-protocol IToByte
  BigDecimal
  (assign-byte [^BigDecimal from ^Byte to]
    (byte from))
  (divide-byte [^BigDecimal divisor ^Byte dividend]
    (-> (BigDecimal. (int dividend))
        (.setScale (.scale divisor))
        (.divide divisor *rounding*)))
  (multiply-byte [^BigDecimal multiplicand ^Byte multiplier]
    (-> (BigDecimal. (int multiplier))
        (.multiply multiplicand)))
  (add-byte [^BigDecimal augend ^Byte addend]
    (-> (BigDecimal. (int addend))
        (.setScale (.scale augend))
        (.add augend)))
  (subtract-byte [^BigDecimal subtrahend ^Byte minuend]
    (-> (BigDecimal. (int minuend))
        (.setScale (.scale subtrahend))
        (.subtract subtrahend)))

  Double
  (assign-byte [^Double from ^Byte to]
    (byte from))
  (divide-byte [^Double divisor ^Byte dividend]
    (core-divide (double dividend) divisor))
  (multiply-byte [^Double multiplicand ^Byte multiplier]
    (core-multiply (double multiplier) multiplicand))
  (add-byte [^Double augend ^Byte addend]
    (core-add (double addend) augend))
  (subtract-byte [^Double subtrahend ^Byte minuend]
    (core-subtract (double minuend) subtrahend))

  Float
  (assign-byte [^Float from ^Byte to]
    (byte from))
  (divide-byte [^Float divisor ^Byte dividend]
    (core-divide (float dividend) divisor))
  (multiply-byte [^Float multiplicand ^Byte multiplier]
    (core-multiply (float multiplier) multiplicand))
  (add-byte [^Float augend ^Byte addend]
    (core-add (float addend) augend))
  (subtract-byte [^Float subtrahend ^Byte minuend]
    (core-subtract (float minuend) subtrahend))

  Long
  (assign-byte [^Long from ^Byte to]
    (byte from))
  (divide-byte [^Long divisor ^Byte dividend]
    (byte (quot dividend divisor)))
  (multiply-byte [^Long multiplicand ^Byte multiplier]
    (core-multiply multiplier multiplicand))
  (add-byte [^Long augend ^Byte addend]
    (core-add addend augend))
  (subtract-byte [^Long subtrahend ^Byte minuend]
    (core-subtract minuend subtrahend))

  Integer
  (assign-byte [^Integer from ^Byte to]
    (byte from))
  (divide-byte [^Integer divisor ^Byte dividend]
    (byte (quot dividend divisor)))
  (multiply-byte [^Integer multiplicand ^Byte multiplier]
    (core-multiply multiplier multiplicand))
  (add-byte [^Integer augend ^Byte addend]
    (core-add addend augend))
  (subtract-byte [^Integer subtrahend ^Byte minuend]
    (core-subtract minuend subtrahend))

  Short
  (assign-byte [^Short from ^Byte to]
    (byte from))
  (divide-byte [^Short divisor ^Byte dividend]
    (int (quot dividend divisor)))
  (multiply-byte [^Short multiplicand ^Byte multiplier]
    (core-multiply multiplier multiplicand))
  (add-byte [^Short augend ^Byte addend]
    (core-add addend augend))
  (subtract-byte [^Short subtrahend ^Byte minuend]
    (core-subtract minuend subtrahend))

  Byte
  (assign-byte [^Byte from ^Byte to]
    from)
  (divide-byte [^Byte divisor ^Byte dividend]
    (short (quot dividend divisor)))
  (multiply-byte [^Byte multiplicand ^Byte multiplier]
    (core-multiply multiplier multiplicand))
  (add-byte [^Byte augend ^Byte addend]
    (core-add addend augend))
  (subtract-byte [^Byte subtrahend ^Byte minuend]
    (core-subtract minuend subtrahend))

  Object
  (assign-byte [from to]
    (incompatible-type from))
  (divide-byte [divisor dividend]
    (incompatible-type dividend))
  (multiply-byte [ multiplicand multiplier]
    (incompatible-type multiplier))
  (add-byte [augend addend]
    (incompatible-type addend))
  (subtract-byte [subtrahend minuend]
    (incompatible-type minuend))

  nil
  (assign-byte [from to]
    from)
  (divide-byte [divisor dividend]
    (throw (NullPointerException.)))
  (multiply-byte [multiplicand multiplier]
    (throw (NullPointerException.)))
  (add-byte [augend addend]
    (throw (NullPointerException.)))
  (subtract-byte [subtrahend minuend]
    (throw (NullPointerException.))))

(defn +
  "Returns the sum of nums. (+) returns 0."
  ([] 0)
  ([x] x)
  ([x y] (op-add x y))
  ([x y & more]
   (reduce + (+ x y) more)))

(defn -
  "If no ys are supplied, returns the negation of x, else subtracts
  the ys from x and returns the result."
  ([x] (core-subtract x))
  ([x y] (op-subtract x y))
  ([x y & more]
   (reduce - (- x y) more)))

(defn *
  "Returns the product of nums. (multiply) returns 1."
  ([] 1)
  ([x] x)
  ([x y] (op-multiply x y))
  ([x y & more]
   (reduce * (* x y) more)))

(defn /
  "If no denominators are supplied, returns 1/numerator,
  else returns numerator divided by all of the denominators."
  ([x] (/ 1 x))
  ([x y] (op-divide x y))
  ([x y & more]
   (reduce / (/ x y) more)))

(def ^:private alter-vars
  [#'clojure.core/+     +      core-add
   #'clojure.core/-     -      core-subtract
   #'clojure.core/*     *      core-multiply
   #'clojure.core//     /      core-divide])

(defn- do-alter-vars!
  [action]
  (doseq [[v set-to reset-to]
          (partition 3 alter-vars)]
    (alter-var-root v
                    (fn [_]
                      (if (= :set action)
                        set-to
                        reset-to)))))

(defn init-global!
  "Alter the root bindings of vars  +, -, * and / to use typeops
  arithmetic operations globally"
  []
  (do-alter-vars! :set))

(defn reset-global!
  "Reset the root bindings of vars  +, -, * and / to use
  clojure.core arithmetic operations globally"
  []
  (do-alter-vars! :reset))
