(ns adserve.server
  (:require
    [compojure.core :refer [GET PUT defroutes]]))

(defroutes app
  (GET "/advertisements" [])
  (PUT "/advertisements" []))
