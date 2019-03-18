(ns adserve.matching.selection
  (:require
    [tabula.util :refer [max-by]]))

;; Selection logic for picking a single from a number of relevant advertisements
;; Various functions for differing strategires

(defn random
  "Randomly selects an element from the collection"
  [ad1 ad2]
  (if (> (rand) 0.5)
    ad1
    ad2))

(defn most-relevant
  [ad1 ad2]
  (max-by :relevancy ad1 ad2))

(defn entropic
  [f]
  (fn [ad1 ad2]
     (if (> (rand) 0.5)
       (f ad1 ad2)
       (f ad2 ad1))))

(defn part
  [selection-fn]
  (fn [state [op what params]]
    (when-not (empty? (get-in state [:database :ad]))
      {:state
       (assoc-in state [:database :ad]
                 (reduce selection-fn (vals (get-in state [:database :ad]))))})))
