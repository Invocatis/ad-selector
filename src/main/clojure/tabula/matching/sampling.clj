(ns tabula.matching.sampling)

;; Logic for selecting a relatively small sample from the whole of advertisements
;; Various functions for differing strategires

(defn random
  "Selects a random sample of size n from the collection coll

   Runs in linear time, should be optimized"
  [n coll & [_]]
  (println n (count coll))
  (when-not (empty? coll)
    (->> coll shuffle (take n))))

(defn relevant
  [coll {:keys [threshold] :or {threshold 1/2}}]
  (filter (fn [{:keys [relevancy]}] (and relevancy (<= threshold relevancy))) coll))

(defn part
  [sampling-fn]
  (fn [state [_ _ params :as command]]
    {:state
     (let [ads (vals (get-in state [:database :ad]))
           sample (sampling-fn ads params)]
       (assoc-in state [:database :ad]
                 (zipmap (map :id sample)
                         sample)))}))
