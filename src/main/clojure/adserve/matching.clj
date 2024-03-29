(ns adserve.matching
  (:require
    [adserve.matching.sampling :as sampling]
    [adserve.matching.relevancy :as relevancy]
    [adserve.matching.selection :as selection]
    [adserve.util :refer [parameter-filter]]
    [tabula.core :as tabula]
    [tabula.engine :as engine]
    [tabula.parts.sieve :refer [parametrized-sieve]]
    [tabula.parts.database :as db]
    [tabula.parts.cache :refer [cache]]
    [tabula.util :refer [filter-by-vals]]
    [clj-time.core :as t]))

(defn times-serviced-to-channel
  [state ad-id channel-id]
  (count (get-in state [:database :services ad-id channel-id])))

(defn times-serviced
  [state ad-id]
  (->> (get-in state [:database :services ad-id])
    vals
    (map count)
    (reduce + 0)))

(defn limits
  [state ad-id]
  (vals (get-in state [:database :limits ad-id])))

(defn within-limit?
  [state ad to-channel {:keys [channel times] :as limit}]
  (cond
    (= channel :*)
    (< (times-serviced state (:id ad)) times)
    (= to-channel channel)
    (< (times-serviced-to-channel state (:id ad) channel) times)
    :else
    true))

(defn within-limits?
  [state ad channel-id]
  (let [limits (vals (get-in state [:database :limits (:id ad)]))]
    (every? (partial within-limit? state ad channel-id) limits)))

(defn limit-sieve
  [state [op what {:keys [channel-id]}]]
  {:state (update-in state [:database :ad]
            (fn [ads]
              (filter-by-vals #(within-limits? state % channel-id) ads)))})

(defn date-sieve
  [state [op what command]]
  {:state (update-in state [:database :ad]
            (fn [ads]
              (filter-by-vals
                (fn [{:keys [start-date end-date]}]
                  (let [now (t/now)]
                    (and (t/before? start-date now)
                         (t/after? end-date now))))
                ads)))})

(def sieve
  (tabula/engine
    (cache date-sieve
      #{:query}
      (fn [ad] (.. (t/now) hourOfDay roundFloorCopy)))
    date-sieve
    limit-sieve
    (parametrized-sieve :ad (parameter-filter [:language]))
    (parametrized-sieve :ad (parameter-filter [:country]))))

(def sieve-cache (cache sieve #{:query}))

(def matcher
  (tabula/engine
    (relevancy/part relevancy/weighted)
    (sampling/part sampling/relevant)))

(def matcher-cache (cache matcher #{:query}))

(def engine|matching
  (tabula/engine
    ; sieve-cache
    matcher-cache
    (selection/part (selection/entropic selection/most-relevant))
    db/query))

(defn match
  [state spec]
  (engine|matching state [:query :ad spec]))
