(ns sampledata
  (:require
    [clojure.string :as s]
    [clojure.java.io :as io]
    [clj-time.core :as t]))

(defn clean-word
  [word]
  (-> word s/lower-case (s/replace #"\s" "_") keyword))

(defn load-data
  [resource]
  (->> resource io/resource slurp s/split-lines (map clean-word) (into [])))

(def languages (load-data "languages.txt"))

(def countries (load-data "countries.txt"))

(def interests (load-data "interests.txt"))

(defn random-date []
  (t/date-midnight (+ (rand-int 2) 2018) (inc (rand-int 12)) (inc (rand 28))))

(defn random-start-end-dates []
  (sort [(random-date) (random-date)]))

(defn ->advert
  [language country interests start-date end-date]
  {:language language :country country :interests (set interests)
   :start-date start-date :end-date end-date})

(defn generate-advert
  [languages countries interests]
  (let [[start-date end-date] (random-start-end-dates)]
    (->advert (rand-nth languages)
              (rand-nth countries)
              (take (inc (rand-int 5)) (shuffle interests))
              start-date
              end-date)))


(defn generate
  [n]
  (into {}
    (map (fn [id ad] [id (assoc ad :id id)])
         (range 1 (inc n))
         (repeatedly n #(generate-advert languages countries interests)))))
