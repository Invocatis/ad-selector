(ns user
  (:require
    [clojure.java.io :as io])
  (:use
    [adserve.core]
    [adserve.action :as act]
    [adserve.matching]
    [sampledata]))

; (def ads (-> "ads.edn" io/resource slurp read-string))

(defn create-ads
  [n]
  (let [ads (generate n)]
    (doall
      (map
        (fn [ad] (act/exec act/execute adserve.core/state [:create :ad {:entry ad}]))
        (vals ads)))))
