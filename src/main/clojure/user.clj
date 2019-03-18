(ns user
  (:require
    [clojure.java.io :as io])
  (:use
    [adserve.core]
    [adserve.action :as action]
    [adserve.matching :as matching]
    [sampledata]))

(def state (atom {}))

(defn create-ads
  [n]
  (let [ads (generate n)]
    (doall
      (map
        (fn [ad] (action/exec action/execute state [:create :ad {:entry ad}]))
        (vals ads))))
  nil)
