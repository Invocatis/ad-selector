(ns user
  (:require
    [clojure.java.io :as io])
  (:use
    [adserve.core]
    [adserve.database]
    [adserve.matching]
    [sampledata]))

(def ads (-> "ads.edn" io/resource slurp read-string))

(def ads (generate 10000))

(defonce x
  (doall
    (map
      (fn [ad] (execute adserve.core/state [:create :ad {:entry ad}]))
      (vals ads))))

(defonce y
  (do
    (swap! adserve.core/state
      assoc-in [:database :limits 1] {1 {:channel 1 :times 3}})
    (swap! adserve.core/state
      assoc-in [:database :services 1 1] {1 {} 2 {} 3 {} 4 {}})))
