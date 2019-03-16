(ns adserve.action
  (:require
    [adserve.matching :refer [match]]))

(defn serve-ad
  [state {:keys [channel-id :as spec]}]
  (let [{:keys [id] :as ad} (match state spec)]
    {:return ad
     :effects [[:create [:database :services id channel-id]
                        {:time (java.util.Date.)}]]}))
