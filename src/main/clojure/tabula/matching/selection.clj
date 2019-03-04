(ns tabula.matching.selection)

;; Selection logic for picking a single from a number of relevant advertisements
;; Various functions for differing strategires

(defn random
  "Randomly selects an element from the collection"
  [coll]
  (rand-nth coll))

(defn part
  [selection-fn]
  (fn [state [op what params]]
    {:state
     (assoc-in state [:database :ad]
               (selection-fn (vals (get-in state [:database :ad]))))}))
