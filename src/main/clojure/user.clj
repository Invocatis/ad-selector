(ns user
  (:require
    [clojure.java.io :as io]
    [tabula.engine :as engine]
    [tabula.parts.id :as id]
    [tabula.parts.database :as db])
  (:use
    [tabula.core]))

(def ads (-> "ads.edn" io/resource slurp read-string))

(defonce x (doall (map (fn [ad] (execute [:create :ad {:entry ad}])) (take 100 (vals ads)))))
