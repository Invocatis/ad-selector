(ns tabula.parts.id-test
  (:require
    [clojure.test :refer :all]
    [tabula.parts.id :refer :all]))

(testing "ID part handler"
  (let [{:keys [command effects]} (handler {} [:create :test {}])
        effect (first effects)]
    (is (= command [:create :test {:id 1}]))
    (is (= (into [] (drop-last effect)) [:APPLY [:database :ids :test]]))
    (is (-> effect last fn?)))
  (let [{:keys [command effects]}
        (handler {:database {:ids {:test 10}}} [:create :test {}])
        effect (first effects)]
    (is (= command [:create :test {:id 10}]))
    (is (= (into [] (drop-last effect)) [:APPLY [:database :ids :test]]))
    (is (-> effect last fn?)))
  (is (nil? (handler {} [:not-create :asdf {}]))))
