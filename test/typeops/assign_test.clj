(ns typeops.assign-test
  (:refer-clojure :exclude [merge assoc])
  (:require [clojure.test :refer :all]
            [typeops.assign :refer :all]))

(def m-types {:String   ""
              :Long     1
              :Integer  (int 0)
              :Decimal  0E-15M
              :Short    (short 0)
              :Decimal2 0.00M
              :Double   0.0
              :Float    (float 0.0)
              :Byte     (byte 0)
              :Date     (java.util.Date.)})

(def m (with-meta m-types {:proto m-types}))

(def incompatible-type #"Incompatible type for operation")

(defn- same-precision
  [k m v e]
  (let [i1 (or (k m)
               (-> m
                   meta
                   :proto
                   k))
        i2 (k (assign m k v))
        c1 (class i1)
        c2 (class i2)]
    (if-not (= c1 c2)
      (throw (IllegalStateException. (str c1 " not same precision as " c2))))
      (if (decimal? i1)
        (or (= (.scale i1) (.scale i2))
            (throw (IllegalStateException. (str i1 " not same scale as " i2)))))
    (or (= i2 e))))

(deftest assign-keep-type
  (testing "decimal"
    (is (same-precision :Decimal m 1.00M 1.00M))
    (is (thrown? IllegalArgumentException (same-precision :Decimal m 1.0 1.00M)))
    (is (thrown? IllegalArgumentException (same-precision :Decimal m (float 1.0) 1.00M)))
    (is (same-precision :Decimal m 1 1.00M))
    (is (same-precision :Decimal m (int 1) 1.00M))
    (is (same-precision :Decimal m (short 1) 1.00M))
    (is (same-precision :Decimal m (byte 1) 1.00M)))
  (testing "decimal-round-up"
    (is (same-precision :Decimal2 m 1.234M 1.23M))
    (is (same-precision :Decimal2 m 1.235M 1.24M)))
  (testing "double"
    (is (thrown? IllegalArgumentException (same-precision :Double m 12.00M 12.0)))
    (is (same-precision :Double m 12.0 12.0))
    (is (same-precision :Double m (float 12.0) 12.0))
    (is (same-precision :Double m 12 12.0))
    (is (same-precision :Double m (int 12) 12.0))
    (is (same-precision :Double m (short 12) 12.0))
    (is (same-precision :Double m (byte 12) 12.0)))
  (testing "float"
    (is (thrown? IllegalArgumentException (same-precision :Float m 12.00M (float 12.0))))
    (is (same-precision :Float m 12.0 (float 12.0)))
    (is (same-precision :Float m (float 12.0) (float 12.0)))
    (is (same-precision :Float m 12 (float 12.0)))
    (is (same-precision :Float m (int 12) (float 12.0)))
    (is (same-precision :Float m (short 12) (float 12.0)))
    (is (same-precision :Float m (byte 12) (float 12.0))))
  (testing "long"
    (is (same-precision :Long m 12.56M 12)) ;truncates
    (is (same-precision :Long m 12.1 12))   ;truncates
    (is (same-precision :Long m (float 12.8) 12)) ;truncates
    (is (same-precision :Long m 12 12))
    (is (same-precision :Long m (int 12) 12))
    (is (same-precision :Long m (short 12) 12))
    (is (same-precision :Long m (byte 12) 12)))
  (testing "int"
    (is (same-precision :Integer m 12.56M 12)) ;truncates
    (is (same-precision :Integer m 12.1 12))   ;truncates
    (is (same-precision :Integer m (float 12.8) 12)) ;truncates
    (is (same-precision :Integer m 12 12))
    (is (same-precision :Integer m (int 12) 12))
    (is (same-precision :Integer m (short 12) 12))
    (is (same-precision :Integer m (byte 12) 12)))
  (testing "short"
    (is (same-precision :Short m 12.56M 12)) ;truncates
    (is (same-precision :Short m 12.1 12))   ;truncates
    (is (same-precision :Short m (float 12.8) 12)) ;truncates
    (is (same-precision :Short m 12 12))
    (is (same-precision :Short m (int 12) 12))
    (is (same-precision :Short m (short 12) 12))
    (is (same-precision :Short m (byte 12) 12)))
  (testing "byte"
    (is (same-precision :Byte m 12.56M 12)) ;truncates
    (is (same-precision :Byte m 12.1 12))   ;truncates
    (is (same-precision :Byte m (float 12.8) 12)) ;truncates
    (is (same-precision :Byte m 12 12))
    (is (same-precision :Byte m (int 12) 12))
    (is (same-precision :Byte m (short 12) 12))
    (is (same-precision :Byte m (byte 12) 12))))

(deftest overflow
  (testing "byte-overflow"
    (is (thrown-with-msg?
          IllegalArgumentException #"out of range for byte"
          (same-precision :Byte m 512 0))))
  (testing "short-overflow"
    (is (thrown-with-msg?
          IllegalArgumentException #"out of range for short"
          (same-precision :Short m 32768 0))))
  (testing "integer-overflow"
    (is (thrown-with-msg?
          IllegalArgumentException #"out of range for int"
          (same-precision :Integer m 2147483648 0))))
  (comment (testing "long-overflow"
    (is (thrown-with-msg?
          IllegalArgumentException #"out of range for long"
          (same-precision :Long m 9223372036854775909M 0))))))

(defn- assign-nil-and-back
  [k m v e]
  (let [m1 (assign m k nil)]
    (same-precision k m1 v e)))

(deftest to-and-from-nil
  (testing "decimal"
    (is (assign-nil-and-back :Decimal m 1.00M 1.00M))
    (is (assign-nil-and-back :Decimal2 m 1.234M 1.23M))
    (is (assign-nil-and-back :Decimal2 m 1.235M 1.24M))))

(deftest incompatible-types
  (testing "incompatible-types"
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Long m "1")))
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Integer m "1")))
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Decimal m "1")))
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Short m "1")))
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Long m "1")))
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Float m "1")))
    (is (thrown-with-msg? IllegalArgumentException incompatible-type
                          (assign m :Byte m "1")))))



(deftest non-numerics
  (testing "not-compatible"
    (is (thrown-with-msg?
          IllegalArgumentException #"not type compatible"
          (assign m :String 3.142)))
    (is (thrown-with-msg?
          IllegalArgumentException #"not type compatible"
          (assign m :Date "Hello, world"))))
  (testing "derived"
    (is (= (-> (assign m :Date (java.sql.Date. 0))
               :Date)
           (java.sql.Date. 0))))
  (testing "derived-and-back"
    (let [d (java.util.Date.)]
      (is (= (-> (assign m :Date (java.sql.Date. 0))
                 (assign :Date d)
                 :Date)
             d)))))
