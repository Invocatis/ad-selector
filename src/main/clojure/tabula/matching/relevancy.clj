(ns tabula.matching.relevancy)

;; Measurement on the how relevant an advert is to a channel
;; Various functions for differing strategires

(defn random
  "Randomly values the relvancy of target to the spec."
  [spec target]
  (rand))

(defn assoc-relevancy
  [f spec ad]
  (assoc ad :relevancy (f spec ad)))

(defn part
  [relevancy-fn]
  (fn [state [_ _ {:keys [spec] :as params}]]
    {:state
     (update-in state [:database :ad]
                (fn [ads]
                  (->> ads
                       (map (fn [[k v]]
                              [k (assoc-relevancy relevancy-fn spec v)]))
                       (into {}))))}))
