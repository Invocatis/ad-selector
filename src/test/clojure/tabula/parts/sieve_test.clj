(ns tabula.parts.sieve-test
  (:require
    [clojure.test :refer :all]
    [tabula.parts.sieve :refer :all]))

(deftest test|sieve
  (is {:state {:a {1 1 3 3 5 5}}}
      (apply (sieve :a odd?)
             [{:a {1 1 2 2 3 3 4 4 5 5}}
              nil])))

(deftest test|parametrized-sieve
  (is {:state {:a {1 1}}}
      (apply (parametrized-sieve :a #(partial = (get-in % [2 :x])))
             [{:a {:1 1 2 2 3 3}}
              [:op :what {:x 1}]])))
