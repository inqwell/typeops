(ns typeops.core-test
  (:require [clojure.test :refer :all]
            [typeops.core :refer :all]))

(deftest operand-1-bigdecimal
  (testing "bigdecimal"
    (is (= (add 3.142M 2.7182818M) 5.8602818M))
    (is (= (subtract 3.142M 2.7182818M 3.142M) -2.7182818M))
    (is (= (multiply 3.142M 2.7182818M 3.142M) 26.8353237278152M))
    (is (= (divide 3.142M 2.7182818M 0.1234M) 9.368M)))


  (testing "double"
    (is (thrown? IllegalArgumentException (add 3.142M 2.718)))
    (is (thrown? IllegalArgumentException (subtract 3.142M 2.718)))
    (is (thrown? IllegalArgumentException (multiply 3.142M 2.718)))
    (is (thrown? IllegalArgumentException (divide 3.142M 2.718))))

  (testing "float"
    (is (thrown? IllegalArgumentException (add 3.142M (float 2.718))))
    (is (thrown? IllegalArgumentException (subtract 3.142M (float 2.718))))
    (is (thrown? IllegalArgumentException (multiply 3.142M (float 2.718))))
    (is (thrown? IllegalArgumentException (divide 3.142M (float 2.718)))))


  (testing "long"
    (is (= (add 3.142M 10) 13.142M))
    (is (= (subtract 3.142M 3 2) -1.858M))
    (is (= (multiply 3.142M 3 2) 18.852M))
    (is (= (divide 2.99792458E+08M 6 12) 4163784M)))


  (testing "integer"
    (is (= (add 3.142M (int 10)) 13.142M))
    (is (= (subtract 3.142M (int 3) (int 2)) -1.858M))
    (is (= (multiply 3.142M (int 3) (int 2)) 18.852M))
    (is (= (divide 2.99792458E+08M (int 6) (int 12)) 4163784M)))


  (testing "short"
    (is (= (add 3.142M (short 10)) 13.142M))
    (is (= (subtract 3.142M (short 3) (short 2)) -1.858M))
    (is (= (multiply 3.142M (short 3) (short 2)) 18.852M))
    (is (= (divide 2.99792458E+08M (short 6) (short 12)) 4163784M)))


  (testing "byte"
    (is (= (add 3.142M (byte 10)) 13.142M))
    (is (= (subtract 3.142M (byte 3) (byte 2)) -1.858M))
    (is (= (multiply 3.142M (byte 3) (byte 2)) 18.852M))
    (is (= (divide 2.99792458E+08M (byte 6) (byte 12)) 4163784M))))



(deftest operand-1-double
  (testing "bigdecimal"
    (is (thrown? IllegalArgumentException (add 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (subtract 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (multiply 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (divide 2.718 3.142M))))

  (testing "double"
    (is (= (add 3.142 2.7182818) 5.8602818)) ;hmmm
    (is (= (type (add 3.142 2.7182818)) (type (double 5.8602818))))
    (is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (subtract 3.142 2.7182818 3.142)) (type (double -2.7182818))))
    (is (= (multiply 3.142 2.7182818 3.142) 26.8353237278152))
    (is (= (type (multiply 3.142 2.7182818 3.142)) (type (double 26.8353237278152))))
    (is (= (divide 3.142 2.7182818 0.1234) 9.36691423227006))
    (is (= (type (divide 3.142 2.7182818 0.1234)) (type (double 9.36691423227006)))))


  (testing "float"
    ;(is (= (add 3.142 (float 2.7182818)) 5.8602818)) ;hmmm
    (is (= (type (add 3.142 (float 2.7182818))) (type (double 5.8602818))))
    ;(is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (subtract 3.142 (float 2.7182818) (float 3.142))) (type (double -2.7182818))))
    ;(is (= (multiply 3.142 (float 2.7182818) (float 3.142)) 26.8353237278152))
    (is (= (type (multiply 3.142 (float 2.7182818) (float 3.142))) (type (double 26.8353237278152))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 9.36691423227006))
    (is (= (type (divide 3.142 (float 2.7182818) (float 0.1234))) (type (double 9.36691423227006)))))


  (testing "long"
    (is (= (add 3.142 10) 13.142))
    (is (= (subtract 3.142 3 2) -1.858))
    (is (= (multiply 3.142 3 2) 18.852))
    (is (= (divide 2.99792458E+08 6 12) 4163784.1388888885)))


  (testing "integer"
    (is (= (add 3.142 (int 10)) 13.142))
    (is (= (subtract 3.142 (int 3) (int 2)) -1.858))
    (is (= (multiply 3.142 (int 3) (int 2)) 18.852))
    (is (= (divide 2.99792458E+08 (int 6) (int 12)) 4163784.1388888885)))


  (testing "short"
    (is (= (add 3.142 (short 10)) 13.142))
    (is (= (subtract 3.142 (short 3) (short 2)) -1.858))
    (is (= (multiply 3.142 (short 3) (short 2)) 18.852))
    (is (= (divide 2.99792458E+08 (short 6) (short 12)) 4163784.1388888885)))


  (testing "byte"
    (is (= (add 3.142 (byte 10)) 13.142))
    (is (= (subtract 3.142 (byte 3) (byte 2)) -1.858))
    (is (= (multiply 3.142 (byte 3) (byte 2)) 18.852))
    (is (= (divide 2.99792458E+08 (byte 6) (byte 12)) 4163784.1388888885))))



(deftest operand-1-float
  (testing "bigdecimal"
    (is (thrown? IllegalArgumentException (add (float 2.718) 3.142M)))
    (is (thrown? IllegalArgumentException (subtract (float 2.718) 3.142M)))
    (is (thrown? IllegalArgumentException (multiply (float 2.718) 3.142M)))
    (is (thrown? IllegalArgumentException (divide (float 2.718) 3.142M))))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.8602818)) ;hmmm
    (is (= (type (add (float 3.142) 2.7182818)) (type (double 5.8602818))))
    ;(is (= (subtract (float 3.142) 2.7182818 3.142) -2.7182818))
    (is (= (type (subtract (float 3.142) 2.7182818 3.142)) (type (double -2.7182818))))
    ;(is (= (multiply (float 3.142) 2.7182818 3.142) 26.8353237278152))
    (is (= (type (multiply (float 3.142) 2.7182818 3.142)) (type (double 26.8353237278152))))
    ;(is (= (divide (float 3.142) 2.7182818 0.1234) 9.36691423227006))
    (is (= (type (divide (float 3.142) 2.7182818 0.1234)) (type (double 9.36691423227006)))))


  (testing "float (produces double)"
    ;(is (= (add 3.142 (float 2.7182818)) 5.8602818)) ;hmmm
    (is (= (type (add 3.142 (float 2.7182818))) (type (double 5.8602818))))
    ;(is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (subtract 3.142 (float 2.7182818) (float 3.142))) (type (double -2.7182818))))
    ;(is (= (multiply 3.142 (float 2.7182818) (float 3.142)) 26.8353237278152))
    (is (= (type (multiply 3.142 (float 2.7182818) (float 3.142))) (type (double 26.8353237278152))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 9.36691423227006))
    (is (= (type (divide 3.142 (float 2.7182818) (float 0.1234))) (type (double 9.36691423227006)))))


  (testing "long (produces double)"
    (is (= (add 3.142 10) 13.142))
    (is (= (subtract 3.142 3 2) -1.858))
    (is (= (multiply 3.142 3 2) 18.852))
    (is (= (divide 2.99792458E+08 6 12) 4163784.1388888885)))


  (testing "integer (produces double)"
    (is (= (add 3.142 (int 10)) 13.142))
    (is (= (subtract 3.142 (int 3) (int 2)) -1.858))
    (is (= (multiply 3.142 (int 3) (int 2)) 18.852))
    (is (= (divide 2.99792458E+08 (int 6) (int 12)) 4163784.1388888885)))


  (testing "short (produces double)"
    (is (= (add 3.142 (short 10)) 13.142))
    (is (= (subtract 3.142 (short 3) (short 2)) -1.858))
    (is (= (multiply 3.142 (short 3) (short 2)) 18.852))
    (is (= (divide 2.99792458E+08 (short 6) (short 12)) 4163784.1388888885)))


  (testing "byte (produces double)"
    (is (= (add 3.142 (byte 10)) 13.142))
    (is (= (subtract 3.142 (byte 3) (byte 2)) -1.858))
    (is (= (multiply 3.142 (byte 3) (byte 2)) 18.852))
    (is (= (divide 2.99792458E+08 (byte 6) (byte 12)) 4163784.1388888885))))

(deftest operand-1-double
  (testing "bigdecimal"
    (is (thrown? IllegalArgumentException (add 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (subtract 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (multiply 2.718 3.142M)))
    (is (thrown? IllegalArgumentException (divide 2.718 3.142M))))

  (testing "double"
    (is (= (add 3.142 2.7182818) 5.8602818)) ;hmmm
    (is (= (type (add 3.142 2.7182818)) (type (double 5.8602818))))
    (is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (subtract 3.142 2.7182818 3.142)) (type (double -2.7182818))))
    (is (= (multiply 3.142 2.7182818 3.142) 26.8353237278152))
    (is (= (type (multiply 3.142 2.7182818 3.142)) (type (double 26.8353237278152))))
    (is (= (divide 3.142 2.7182818 0.1234) 9.36691423227006))
    (is (= (type (divide 3.142 2.7182818 0.1234)) (type (double 9.36691423227006)))))


  (testing "float"
    ;(is (= (add 3.142 (float 2.7182818)) 5.8602818)) ;hmmm
    (is (= (type (add 3.142 (float 2.7182818))) (type (double 5.8602818))))
    ;(is (= (subtract 3.142 2.7182818 3.142) -2.7182818))
    (is (= (type (subtract 3.142 (float 2.7182818) (float 3.142))) (type (double -2.7182818))))
    ;(is (= (multiply 3.142 (float 2.7182818) (float 3.142)) 26.8353237278152))
    (is (= (type (multiply 3.142 (float 2.7182818) (float 3.142))) (type (double 26.8353237278152))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 9.36691423227006))
    (is (= (type (divide 3.142 (float 2.7182818) (float 0.1234))) (type (double 9.36691423227006)))))


  (testing "long"
    (is (= (add 3.142 10) 13.142))
    (is (= (subtract 3.142 3 2) -1.858))
    (is (= (multiply 3.142 3 2) 18.852))
    (is (= (divide 2.99792458E+08 6 12) 4163784.1388888885)))


  (testing "integer"
    (is (= (add 3.142 (int 10)) 13.142))
    (is (= (subtract 3.142 (int 3) (int 2)) -1.858))
    (is (= (multiply 3.142 (int 3) (int 2)) 18.852))
    (is (= (divide 2.99792458E+08 (int 6) (int 12)) 4163784.1388888885)))


  (testing "short"
    (is (= (add 3.142 (short 10)) 13.142))
    (is (= (subtract 3.142 (short 3) (short 2)) -1.858))
    (is (= (multiply 3.142 (short 3) (short 2)) 18.852))
    (is (= (divide 2.99792458E+08 (short 6) (short 12)) 4163784.1388888885)))


  (testing "byte"
    (is (= (add 3.142 (byte 10)) 13.142))
    (is (= (subtract 3.142 (byte 3) (byte 2)) -1.858))
    (is (= (multiply 3.142 (byte 3) (byte 2)) 18.852))
    (is (= (divide 2.99792458E+08 (byte 6) (byte 12)) 4163784.1388888885))))



(deftest operand-1-long
  (testing "bigdecimal"
    (is (= (add 10 3.142M) 13.142M))
    (is (= (subtract 3 2 3.142M) -2.142M))
    (is (= (multiply 3 2 3.142M) 18.852M))
    (is (= (divide 299792458 6M 12.00M) 4163784M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (add 3 2.7182818)) (type (double 5.7182818))))
    (is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract 3 2.7182818 3.142)) (type (double -2.8602818))))
    (is (= (multiply 3 2.7182818 3.142) 25.6225242468))
    (is (= (type (multiply 3 2.7182818 3.142)) (type (double 25.6225242468))))
    (is (= (divide 3 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (divide 3 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (add 3 (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract 3 (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (multiply 3 (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (divide 3 (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (add 3 10) 13))
    (is (= (add 3 10 12 14) 39))
    (is (= (subtract 3 3 -2) 2))
    (is (= (multiply 3 3 2) 18))
    (is (= (divide 299792458 6 12) 4163784)))


  (testing "integer (produces long)"
    (is (= (add 3 (int 10)) 13))
    (is (= (subtract 3 (int 3) (int 2)) -2))
    (is (= (multiply 3 (int 3) (int 2)) 18))
    (is (= (divide 299792458 (int 6) (int 12)) 4163784)))


  (testing "short (produces long)"
    (is (= (add 3 (short 10)) 13))
    (is (= (subtract 3 (short 3) (short 2)) -2))
    (is (= (multiply 3 (short 3) (short 2)) 18))
    (is (= (divide 299792458 (short 6) (short 12)) 4163784)))


  (testing "byte (produces long)"
    (is (= (add 3 (byte 10)) 13))
    (is (= (subtract 3 (byte 3) (byte 2)) -2))
    (is (= (multiply 3 (byte 3) (byte 2)) 18))
    (is (= (divide 299792458 (byte 6) (byte 12)) 4163784))))

(deftest operand-1-integer
  (testing "bigdecimal"
    (is (= (add (int 10) 3.142M) 13.142M))
    (is (= (subtract (int 3) (int 2) 3.142M) -2.142M))
    (is (= (multiply (int 3) (int 2) 3.142M) 18.852M))
    (is (= (divide (int 299792458) 6M 12.00M) 4163784M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (add (int 3) 2.7182818)) (type (double 5.7182818))))
    ;(is (= (subtract (int 3) 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract (int 3) 2.7182818 3.142)) (type (double -2.8602818))))
    ;(is (= (multiply (int 3) 2.7182818 3.142) 25.6225242468))
    (is (= (type (multiply (int 3) 2.7182818 3.142)) (type (double 25.6225242468))))
    ;(is (= (divide (int 3) 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (divide (int 3) 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (add (int 3) (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract (int 3) (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (multiply (int 3) (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (divide (int 3) (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (add (int 3) 10) 13))
    (is (= (add (int 3) 10 12 14) 39))
    (is (= (subtract (int 3) 3 -2) 2))
    (is (= (multiply (int 3) 3 2) 18))
    (is (= (divide (int 299792458) 6 12) 4163784)))


  (testing "integer (produces long)"
    (is (= (add (int 3) (int 10)) 13))
    (is (= (subtract (int 3) (int 3) (int 2)) -2))
    (is (= (multiply (int 3) (int 3) (int 2)) 18))
    (is (= (divide (int 299792458) (int 6) (int 12)) 4163784)))


  (testing "short (produces long)"
    (is (= (add (int 3) (short 10)) 13))
    (is (= (subtract (int 3) (short 3) (short 2)) -2))
    (is (= (multiply (int 3) (short 3) (short 2)) 18))
    (is (= (divide (int 299792458) (short 6) (short 12)) 4163784)))


  (testing "byte (produces long)"
    (is (= (add (int 3) (byte 10)) 13))
    (is (= (subtract (int 3) (byte 3) (byte 2)) -2))
    (is (= (multiply (int 3) (byte 3) (byte 2)) 18))
    (is (= (divide (int 299792458) (byte 6) (byte 12)) 4163784))))

(deftest operand-1-short
  (testing "bigdecimal"
    (is (= (add (short 10) 3.142M) 13.142M))
    (is (= (subtract (short 3) (short 2) 3.142M) -2.142M))
    (is (= (multiply (short 3) (short 2) 3.142M) 18.852M))
    (is (= (divide (short 12345) 6.000M 12.00M) 171.458M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (add (short 3) 2.7182818)) (type (double 5.7182818))))
    ;(is (= (subtract (short 3) 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract (short 3) 2.7182818 3.142)) (type (double -2.8602818))))
    ;(is (= (multiply (short 3) 2.7182818 3.142) 25.6225242468))
    (is (= (type (multiply (short 3) 2.7182818 3.142)) (type (double 25.6225242468))))
    ;(is (= (divide (short 3) 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (divide (short 3) 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (add (short 3) (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract (short 3) (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (multiply (short 3) (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (divide (short 3) (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (add (short 3) 10) 13))
    (is (= (add (short 3) 10 12 14) 39))
    (is (= (subtract (short 3) 3 -2) 2))
    (is (= (multiply (short 3) 3 2) 18))
    (is (= (divide (short 12345) 6 12) 171)))


  (testing "integer (produces long)"
    (is (= (add (short 3) (int 10)) 13))
    (is (= (subtract (short 3) (int 3) (int 2)) -2))
    (is (= (multiply (short 3) (int 3) (int 2)) 18))
    (is (= (divide (short 12345) (int 6) (int 12)) 171)))


  (testing "short (produces long)"
    (is (= (add (short 3) (short 10)) 13))
    (is (= (subtract (short 3) (short 3) (short 2)) -2))
    (is (= (multiply (short 3) (short 3) (short 2)) 18))
    (is (= (divide (short 12345) (short 6) (short 12)) 171)))


  (testing "byte (produces long)"
    (is (= (add (short 3) (byte 10)) 13))
    (is (= (subtract (short 3) (byte 3) (byte 2)) -2))
    (is (= (multiply (short 3) (byte 3) (byte 2)) 18))
    (is (= (divide (short 12345) (byte 6) (byte 12)) 171))))

(deftest operand-1-byte
  (testing "bigdecimal"
    (is (= (add (byte 10) 3.142M) 13.142M))
    (is (= (subtract (byte 3) (byte 2) 3.142M) -2.142M))
    (is (= (multiply (byte 3) (byte 2) 3.142M) 18.852M))
    (is (= (divide (byte 123) 6.000M 12.00M) 1.708M)))

  (testing "double (produces double)"
    ;(is (= (add (float 3.142) 2.7182818) 5.7182818)) ;hmmm
    (is (= (type (add (byte 3) 2.7182818)) (type (double 5.7182818))))
    ;(is (= (subtract (byte 3) 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract (byte 3) 2.7182818 3.142)) (type (double -2.8602818))))
    ;(is (= (multiply (byte 3) 2.7182818 3.142) 25.6225242468))
    (is (= (type (multiply (byte 3) 2.7182818 3.142)) (type (double 25.6225242468))))
    ;(is (= (divide (byte 3) 2.7182818 0.1234) 8.943584562956774))
    (is (= (type (divide (byte 3) 2.7182818 0.1234)) (type (double 8.943584562956774)))))

  (testing "float (produces double)"
    ;(is (= (add 3 (float 2.7182818)) 5.7182817459106445)) ;hmmm
    (is (= (type (add (byte 3) (float 2.7182818))) (type (double 5.7182817459106445))))
    ;(is (= (subtract 3 2.7182818 3.142) -2.8602818))
    (is (= (type (subtract (byte 3) (float 2.7182818) (float 3.142))) (type (double -2.8602818))))
    ;(is (= (multiply 3 (float 2.7182818) (float 3.142)) 25.622523410316944))
    (is (= (type (multiply (byte 3) (float 2.7182818) (float 3.142))) (type (double 25.622523410316944))))
    ;(is (= (divide 3.142 (float 2.7182818) (float 0.1234)) 8.94358454393072))
    (is (= (type (divide (byte 3) (float 2.7182818) (float 0.1234))) (type (double 8.94358454393072)))))


  (testing "long"
    (is (= (add (byte 3) 10) 13))
    (is (= (add (byte 3) 10 12 14) 39))
    (is (= (subtract (byte 3) 3 -2) 2))
    (is (= (multiply (byte 3) 3 2) 18))
    (is (= (divide (byte 123) 6 3) 6)))


  (testing "integer (produces long)"
    (is (= (add (byte 3) (int 10)) 13))
    (is (= (subtract (byte 3) (int 3) (int 2)) -2))
    (is (= (multiply (byte 3) (int 3) (int 2)) 18))
    (is (= (divide (byte 123) (int 6) (int 3)) 6)))


  (testing "short (produces long)"
    (is (= (add (byte 3) (short 10)) 13))
    (is (= (subtract (byte 3) (short 3) (short 2)) -2))
    (is (= (multiply (byte 3) (short 3) (short 2)) 18))
    (is (= (divide (byte 123) (short 6) (short 3)) 6)))


  (testing "byte (produces long)"
    (is (= (add (byte 3) (byte 10)) 13))
    (is (= (subtract (byte 3) (byte 3) (byte 2)) -2))
    (is (= (multiply (byte 3) (byte 3) (byte 2)) 18))
    (is (= (divide (byte 123) (byte 6) (byte 3)) 6))))

(deftest unary-or-none
  (testing "add none"
    (is (= (add) 0))
    (is (= (add 2) 2))
    (is (= (subtract 2) -2))
    (is (= (multiply) 1))
    (is (= (multiply 2) 2))
    (is (= (divide 0.5) 2.0))))
