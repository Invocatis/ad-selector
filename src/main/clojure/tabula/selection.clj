(ns tabula.selection)

;; Selection logic for picking a single from a number of relevant advertisements
;; Various functions for differing strategires

(defn random
  "Randomly selects an element from the collection"
  [coll]
  (rand-nth coll))
