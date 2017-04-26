(ns typeops.symbols-test
  (:require [clojure.test :refer :all]
            [typeops.core :refer :all]))

(deftest syms-in-namespace
  (testing "plus-etc"
    (do
      (init-namespace)
      (is (and (= add +)
               (= subtract -)
               (= multiply *)
               (= divide /))))))
