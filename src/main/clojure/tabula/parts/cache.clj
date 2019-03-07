(ns tabula.parts.cache
  (:require
    [tabula.util :refer [contains-in?]]))

(defn cache
  [part opset & [information-content]]
  (fn [state [op :as command]]
    (when (contains? opset op)
      (let [ic (or information-content identity)
            info (ic command)]
        (if (contains-in? state [::cache part info])
            {:state (assoc state :database (get-in state [::cache part info]))}
          (let [result (apply part [state info])
                effect [:PUT [::cache part info] (get-in result [:state :database])]]
            (assoc result :effects
              (if (:effects result)
                (conj (:effects result) effect)
                [effect]))))))))
