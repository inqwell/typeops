(ns typeops.core-test
  (:refer-clojure :exclude [+ - * /])
  (:require [clojure.test :refer :all]
            [typeops.core :refer :all]))

(deftest operand-1-bigdecimal
  (testing "bigdecimal"
    (is (= (+ 3.142M 2.7182818M) 5.8602818M))
    (is (= (- 3.142M 2.7182818M 3.142M) -2.7182818M))
    (is (= (* 3.142M 2.7182818M 3.142M) 26.8353237278152M))
    (is (= (/ 3.142M 2.7182818M 0.1234M) 9.368M)))


  (testing "double"
    (is (thrown? IllegalArgumentException (+ 3.142M 2.718)))
    (is (thrown? IllegalArgumentException (- 3.142M 2.718)))
    (is (thrown? IllegalArgumentException (* 3.142M 2.718)))
    (is (thrown? IllegalArgumentException (/ 3.142M 2.718))))

  (testing "float"
    (is (thrown? IllegalArgumentException (+ 3.142M (float 2.718))))
    (is (thrown? IllegalArgumentException (- 3.142M (float 2.718))))
    (is (thrown? IllegalArgumentException (* 3.142M (float 2.718))))
    (is (thrown? IllegalArgumentException (/ 3.142M (float 2.718)))))


  (testing "long"
    (is (= (+ 3.142M 10) 13.142M))
    (is (= (- 3.142M 3 2) -1.858M))
    (is (= (* 3.142M 3 2) 18.852M))
    (is (= (/ 2.99792458E+08M 6 12) 4163784M)))


  (testing "integer"
    (is (= (+ 3.142M (int 10)) 13.142M))
    (is (= (- 3.142M (int 3) (int 2)) -1.858M))
    (is (= (* 3.142M (int 3) (int 2)) 18.852M))
    (is (= (/ 2.99792458E+08M (int 6) (int 12)) 4163784M)))


  (testing "short"
    (is (= (+ 3.142M (short 10)) 13.142M))
    (is (= (- 3.142M (short 3) (short 2)) -1.858M))
    (is (= (* 3.142M (short 3) (short 2)) 18.852M))
    (is (= (/ 2.99792458E+08M (short 6) (short 12)) 4163784M)))


  (testing "byte"
    (is (= (+ 3.142M (byte 10)) 13.142M))
    (is (= (- 3.142M (byte 3) (byte 2)) -1.858M))
    (is (= (* 3.142M (byte 3) (byte 2)) 18.852M))
    (is (= (/ 2.99792458E+08M (byte 6) (byte 12)) 4163784M))))



(deftest operand-1-double
  (testing "bigdecimal"
    (is (thrown? IllegalArgumentException (+ 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (- 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (* 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (/ 2.718 3.142M))))

  (testing "double"
    (is (= (+ 3.142 2.7182818) 5.8602818)) ;hmmm
    (is (= (type (+ 3.142 2.7182818)) (type (double 5.8602818))))
    (is (= (- 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (- 3.142 2.7182818 3.142)) (type (double -2.7182818))))
    (is (= (* 3.142 2.7182818 3.142) 26.8353237278152))
    (is (= (type (* 3.142 2.7182818 3.142)) (type (double 26.8353237278152))))
    (is (= (/ 3.142 2.7182818 0.1234) 9.36691423227006))
    (is (= (type (/ 3.142 2.7182818 0.1234)) (type (double 9.36691423227006)))))


  (testing "float"
    ;(is (= (add 3.142 (float 2.7182818)) 5.8602818)) ;hmmm
    (is (= (type (+ 3.142 (float 2.7182818))) (type (double 5.8602818))))
    ;(is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (- 3.142 (float 2.7182818) (float 3.142))) (type (double -2.7182818))))
    ;(is (= (multiply 3.142 (float 2.7182818) (float 3.142)) 26.8353237278152))
    (is (= (type (* 3.142 (float 2.7182818) (float 3.142))) (type (double 26.8353237278152))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 9.36691423227006))
    (is (= (type (/ 3.142 (float 2.7182818) (float 0.1234))) (type (double 9.36691423227006)))))


  (testing "long"
    (is (= (+ 3.142 10) 13.142))
    (is (= (- 3.142 3 2) -1.858))
    (is (= (* 3.142 3 2) 18.852))
    (is (= (/ 2.99792458E+08 6 12) 4163784.1388888885)))


  (testing "integer"
    (is (= (+ 3.142 (int 10)) 13.142))
    (is (= (- 3.142 (int 3) (int 2)) -1.858))
    (is (= (* 3.142 (int 3) (int 2)) 18.852))
    (is (= (/ 2.99792458E+08 (int 6) (int 12)) 4163784.1388888885)))


  (testing "short"
    (is (= (+ 3.142 (short 10)) 13.142))
    (is (= (- 3.142 (short 3) (short 2)) -1.858))
    (is (= (* 3.142 (short 3) (short 2)) 18.852))
    (is (= (/ 2.99792458E+08 (short 6) (short 12)) 4163784.1388888885)))


  (testing "byte"
    (is (= (+ 3.142 (byte 10)) 13.142))
    (is (= (- 3.142 (byte 3) (byte 2)) -1.858))
    (is (= (* 3.142 (byte 3) (byte 2)) 18.852))
    (is (= (/ 2.99792458E+08 (byte 6) (byte 12)) 4163784.1388888885))))



(deftest operand-1-float
  (testing "bigdecimal"
    (is (thrown? IllegalArgumentException (+ (float 2.718) 3.142M)))
    (is (thrown? IllegalArgumentException (- (float 2.718) 3.142M)))
    (is (thrown? IllegalArgumentException (* (float 2.718) 3.142M)))
    (is (thrown? IllegalArgumentException (/ (float 2.718) 3.142M))))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.8602818)) ;hmmm
    (is (= (type (+ (float 3.142) 2.7182818)) (type (double 5.8602818))))
    ;(is (= (subtract (float 3.142) 2.7182818 3.142) -2.7182818))
    (is (= (type (- (float 3.142) 2.7182818 3.142)) (type (double -2.7182818))))
    ;(is (= (multiply (float 3.142) 2.7182818 3.142) 26.8353237278152))
    (is (= (type (* (float 3.142) 2.7182818 3.142)) (type (double 26.8353237278152))))
    ;(is (= (divide (float 3.142) 2.7182818 0.1234) 9.36691423227006))
    (is (= (type (/ (float 3.142) 2.7182818 0.1234)) (type (double 9.36691423227006)))))


  (testing "float (produces double)"
    ;(is (= (add 3.142 (float 2.7182818)) 5.8602818)) ;hmmm
    (is (= (type (+ 3.142 (float 2.7182818))) (type (double 5.8602818))))
    ;(is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (- 3.142 (float 2.7182818) (float 3.142))) (type (double -2.7182818))))
    ;(is (= (multiply 3.142 (float 2.7182818) (float 3.142)) 26.8353237278152))
    (is (= (type (* 3.142 (float 2.7182818) (float 3.142))) (type (double 26.8353237278152))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 9.36691423227006))
    (is (= (type (/ 3.142 (float 2.7182818) (float 0.1234))) (type (double 9.36691423227006)))))


  (testing "long (produces double)"
    ; (is (= (+ (float 3.142) 10) 13.142)) not accurate enough!
    (is (= (type (+ (float 3.142) 10)) (type (double 13.142))))
    ; (is (= (- (float 3.142) 3 2) -1.858))
    (is (= (type (- (float 3.142) 3 2)) (type (double -1.858))))
    ; (is (= (* (float 3.142) 3 2) 18.852))
    (is (= (type (* (float 3.142) 3 2)) (type (double 18.852))))
    ; (is (= (/ (float 2.99792458E+08) 6 12) 4163784.0))
    (is (= (type (/ (float 2.99792458E+08) 6 12)) (type (double 4163784.0)))))


  (testing "integer (produces double)"
    ; (is (= (+ (float 3.142) (int 10)) 13.142))
    (is (= (type (+ (float 3.142) (int 10))) (type (double 13.142))))
    ; (is (= (- (float 3.142) (int 3) (int 2)) -1.858))
    (is (= (type (- (float 3.142) (int 3) (int 2))) (type (double -1.858))))
    ; (is (= (* (float 3.142) (int 3) (int 2)) 18.852))
    (is (= (type (* (float 3.142) (int 3) (int 2))) (type (double 18.852))))
    (is (= (/ (float 2.99792458E+08) (int 6) (int 12)) 4163784.0)))


  (testing "short (produces double)"
    ; (is (= (+ (float 3.142) (short 10)) 13.142))
    (is (= (type (+ (float 3.142) (short 10))) (type (double 13.142))))
    ; (is (= (- (float 3.142) (short 3) (short 2)) -1.858))
    (is (= (type (- (float 3.142) (short 3) (short 2))) (type (double -1.858))))
    ; (is (= (* (float 3.142) (short 3) (short 2)) 18.852))
    (is (= (type (* (float 3.142) (short 3) (short 2))) (type (double 18.852))))
    (is (= (/ (float 2.99792458E+08) (short 6) (short 12)) 4163784.0)))


  (testing "byte (produces double)"
    ; (is (= (+ (float 3.142) (byte 10)) 13.142))
    (is (= (type (+ (float 3.142) (byte 10))) (type (double 13.142))))
    ; (is (= (- (float 3.142) (byte 3) (byte 2)) -1.858))
    (is (= (type (- (float 3.142) (byte 3) (byte 2))) (type (double -1.858))))
    ; (is (= (* (float 3.142) (byte 3) (byte 2)) 18.852))
    (is (= (type (* (float 3.142) (byte 3) (byte 2))) (type (double 18.852))))
    (is (= (/ (float 2.99792458E+08) (byte 6) (byte 12)) 4163784.0))))

(deftest operand-1-long
  (testing "bigdecimal"
    (is (= (+ 10 3.142M) 13.142M))
    (is (= (- 3 2 3.142M) -2.142M))
    (is (= (* 3 2 3.142M) 18.852M))
    (is (= (/ 299792458 6M 12.00M) 4163784M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (+ 3 2.7182818)) (type (double 5.7182818))))
    (is (= (- 3 2.7182818 3.142) -2.8602818))
    (is (= (type (- 3 2.7182818 3.142)) (type (double -2.8602818))))
    (is (= (* 3 2.7182818 3.142) 25.6225242468))
    (is (= (type (* 3 2.7182818 3.142)) (type (double 25.6225242468))))
    (is (= (/ 3 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (/ 3 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (+ 3 (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (- 3 (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (* 3 (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (/ 3 (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (+ 3 10) 13))
    (is (= (+ 3 10 12 14) 39))
    (is (= (- 3 3 -2) 2))
    (is (= (* 3 3 2) 18))
    (is (= (/ 299792458 6 12) 4163784)))


  (testing "integer (produces long)"
    (is (= (+ 3 (int 10)) 13))
    (is (= (- 3 (int 3) (int 2)) -2))
    (is (= (* 3 (int 3) (int 2)) 18))
    (is (= (/ 299792458 (int 6) (int 12)) 4163784)))


  (testing "short (produces long)"
    (is (= (+ 3 (short 10)) 13))
    (is (= (- 3 (short 3) (short 2)) -2))
    (is (= (* 3 (short 3) (short 2)) 18))
    (is (= (/ 299792458 (short 6) (short 12)) 4163784)))


  (testing "byte (produces long)"
    (is (= (+ 3 (byte 10)) 13))
    (is (= (- 3 (byte 3) (byte 2)) -2))
    (is (= (* 3 (byte 3) (byte 2)) 18))
    (is (= (/ 299792458 (byte 6) (byte 12)) 4163784))))

(deftest operand-1-integer
  (testing "bigdecimal"
    (is (= (+ (int 10) 3.142M) 13.142M))
    (is (= (- (int 3) (int 2) 3.142M) -2.142M))
    (is (= (* (int 3) (int 2) 3.142M) 18.852M))
    (is (= (/ (int 299792458) 6M 12.00M) 4163784M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (+ (int 3) 2.7182818)) (type (double 5.7182818))))
    ;(is (= (subtract (int 3) 2.7182818 3.142) -2.8602818))
    (is (= (type (- (int 3) 2.7182818 3.142)) (type (double -2.8602818))))
    ;(is (= (multiply (int 3) 2.7182818 3.142) 25.6225242468))
    (is (= (type (* (int 3) 2.7182818 3.142)) (type (double 25.6225242468))))
    ;(is (= (divide (int 3) 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (/ (int 3) 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (+ (int 3) (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (- (int 3) (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (* (int 3) (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (/ (int 3) (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (+ (int 3) 10) 13))
    (is (= (+ (int 3) 10 12 14) 39))
    (is (= (- (int 3) 3 -2) 2))
    (is (= (* (int 3) 3 2) 18))
    (is (= (/ (int 299792458) 6 12) 4163784)))


  (testing "integer (produces long)"
    (is (= (+ (int 3) (int 10)) 13))
    (is (= (- (int 3) (int 3) (int 2)) -2))
    (is (= (* (int 3) (int 3) (int 2)) 18))
    (is (= (/ (int 299792458) (int 6) (int 12)) 4163784)))


  (testing "short (produces long)"
    (is (= (+ (int 3) (short 10)) 13))
    (is (= (- (int 3) (short 3) (short 2)) -2))
    (is (= (* (int 3) (short 3) (short 2)) 18))
    (is (= (/ (int 299792458) (short 6) (short 12)) 4163784)))


  (testing "byte (produces long)"
    (is (= (+ (int 3) (byte 10)) 13))
    (is (= (- (int 3) (byte 3) (byte 2)) -2))
    (is (= (* (int 3) (byte 3) (byte 2)) 18))
    (is (= (/ (int 299792458) (byte 6) (byte 12)) 4163784))))

(deftest operand-1-short
  (testing "bigdecimal"
    (is (= (+ (short 10) 3.142M) 13.142M))
    (is (= (- (short 3) (short 2) 3.142M) -2.142M))
    (is (= (* (short 3) (short 2) 3.142M) 18.852M))
    (is (= (/ (short 12345) 6.000M 12.00M) 171.458M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (+ (short 3) 2.7182818)) (type (double 5.7182818))))
    ;(is (= (subtract (short 3) 2.7182818 3.142) -2.8602818))
    (is (= (type (- (short 3) 2.7182818 3.142)) (type (double -2.8602818))))
    ;(is (= (multiply (short 3) 2.7182818 3.142) 25.6225242468))
    (is (= (type (* (short 3) 2.7182818 3.142)) (type (double 25.6225242468))))
    ;(is (= (divide (short 3) 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (/ (short 3) 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (+ (short 3) (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (- (short 3) (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (* (short 3) (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (/ (short 3) (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (+ (short 3) 10) 13))
    (is (= (+ (short 3) 10 12 14) 39))
    (is (= (- (short 3) 3 -2) 2))
    (is (= (* (short 3) 3 2) 18))
    (is (= (/ (short 12345) 6 12) 171)))


  (testing "integer (produces long)"
    (is (= (+ (short 3) (int 10)) 13))
    (is (= (- (short 3) (int 3) (int 2)) -2))
    (is (= (* (short 3) (int 3) (int 2)) 18))
    (is (= (/ (short 12345) (int 6) (int 12)) 171)))


  (testing "short (produces long)"
    (is (= (+ (short 3) (short 10)) 13))
    (is (= (- (short 3) (short 3) (short 2)) -2))
    (is (= (* (short 3) (short 3) (short 2)) 18))
    (is (= (/ (short 12345) (short 6) (short 12)) 171)))


  (testing "byte (produces long)"
    (is (= (+ (short 3) (byte 10)) 13))
    (is (= (- (short 3) (byte 3) (byte 2)) -2))
    (is (= (* (short 3) (byte 3) (byte 2)) 18))
    (is (= (/ (short 12345) (byte 6) (byte 12)) 171))))

(deftest operand-1-byte
  (testing "bigdecimal"
    (is (= (+ (byte 10) 3.142M) 13.142M))
    (is (= (- (byte 3) (byte 2) 3.142M) -2.142M))
    (is (= (* (byte 3) (byte 2) 3.142M) 18.852M))
    (is (= (/ (byte 123) 6.000M 12.00M) 1.708M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (+ (byte 3) 2.7182818)) (type (double 5.7182818))))
    ;(is (= (subtract (byte 3) 2.7182818 3.142) -2.8602818))
    (is (= (type (- (byte 3) 2.7182818 3.142)) (type (double -2.8602818))))
    ;(is (= (multiply (byte 3) 2.7182818 3.142) 25.6225242468))
    (is (= (type (* (byte 3) 2.7182818 3.142)) (type (double 25.6225242468))))
    ;(is (= (divide (byte 3) 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (/ (byte 3) 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (+ (byte 3) (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (- (byte 3) (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (* (byte 3) (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (/ (byte 3) (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (+ (byte 3) 10) 13))
    (is (= (+ (byte 3) 10 12 14) 39))
    (is (= (- (byte 3) 3 -2) 2))
    (is (= (* (byte 3) 3 2) 18))
    (is (= (/ (byte 123) 6 3) 6)))


  (testing "integer (produces long)"
    (is (= (+ (byte 3) (int 10)) 13))
    (is (= (- (byte 3) (int 3) (int 2)) -2))
    (is (= (* (byte 3) (int 3) (int 2)) 18))
    (is (= (/ (byte 123) (int 6) (int 3)) 6)))


  (testing "short (produces long)"
    (is (= (+ (byte 3) (short 10)) 13))
    (is (= (- (byte 3) (short 3) (short 2)) -2))
    (is (= (* (byte 3) (short 3) (short 2)) 18))
    (is (= (/ (byte 123) (short 6) (short 3)) 6)))


  (testing "byte (produces long)"
    (is (= (+ (byte 3) (byte 10)) 13))
    (is (= (- (byte 3) (byte 3) (byte 2)) -2))
    (is (= (* (byte 3) (byte 3) (byte 2)) 18))
    (is (= (/ (byte 123) (byte 6) (byte 3)) 6))))

(deftest unary-or-none
  (testing "add none"
    (is (= (+) 0))
    (is (= (+ 2) 2))
    (is (= (- 2) -2))
    (is (= (*) 1))
    (is (= (* 2) 2))
    (is (= (/ 0.5) 2.0))))
