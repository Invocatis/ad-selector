(ns user
  (:require
    [clojure.java.io :as io]
    [tabula.engine :as engine]
    [tabula.parts.id :as id]
    [tabula.parts.database :as db])
  (:use
    [tabula.core]
    [sampledata]))

(def ads (-> "ads.edn" io/resource slurp read-string))

(def ads (generate 10000))

(defonce x (doall (map (fn [ad] (execute [:create :ad {:entry ad}])) (vals ads))))
