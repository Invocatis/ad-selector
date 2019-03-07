(ns tabula.engine-test
  (:require
    [clojure.test :refer :all]
    [tabula.engine :refer :all]))

(deftest test|put
  (testing "Testing Commit Put"
    (is (= {1 1} (commit|put {} [:PUT [1] 1])))
    (is (= {1 {1 {1 1}}} (commit|put {} [:PUT [1 1 1] 1])))))

(deftest test|patch
  (testing "Testing Patch"
    (is (= {1 [1]} (commit|patch {1 []} [:PATCH [1] 1])))
    (is (= {1 {1 1}} (commit|patch {1 {}} [:PATCH [1] [1 1]])))
    (is (= {1 {1 1}} (commit|patch {1 {}} [:PATCH [1] {1 1}])))))

(deftest test|apply
  (testing "Testing Apply"
    (is (= {1 2} (commit|apply {1 1} [:APPLY [1] inc])))))

(deftest test|remove
  (testing "Testing Remove"
    (is (= {1 {}} (commit|remove {1 {1 1}} [:REMOVE [1 1]])))))

(deftest test|commit
  (testing "Testing Commit"
    (is (= {1 1} (commit {} [:PUT [1] 1])))
    (is (= {1 {1 1}} (commit {1 {}} [:PATCH [1] [1 1]])))
    (is (= {1 2} (commit {1 1} [:APPLY [1] inc])))
    (is (= {} (commit {1 1} [:REMOVE [1]])))))
