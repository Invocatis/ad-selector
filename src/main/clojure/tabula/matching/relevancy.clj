(ns tabula.matching.relevancy)

;; Measurement on the how relevant an advert is to a channel
;; Various functions for differing strategires

(defn random
  "Randomly values the relvancy of target to the spec."
  [spec target]
  (rand))

(defn weighted
  [spec target]
  (if spec
    (let [keyset (:interests target)]
      (/ (reduce +
                 (map (fn [[k v]] (if (contains? keyset k) v 0)) spec))
         (reduce + (vals spec))))
    1))

(defn assoc-relevancy
  [f spec ad]
  (assoc ad :relevancy (f spec ad)))

(defn part
  [relevancy-fn]
  (fn [state [_ _ {:keys [interests] :as params}]]
    (when interests
      {:state
       (update-in state [:database :ad]
                  (fn [ads]
                    (->> ads
                         (map (fn [[k v]]
                                [k (assoc-relevancy relevancy-fn interests v)]))
                         (into {}))))})))
