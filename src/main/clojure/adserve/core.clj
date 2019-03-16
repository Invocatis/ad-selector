(ns adserve.core
  (:require
    [adserve.matching :refer [engine|matching]]
    [adserve.database :refer [engine|query engine|execution]]))


(defonce state (atom {:engines {:execution engine|execution
                                :query engine|query
                                :matching engine|matching}}))
