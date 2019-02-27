(ns sampledata
  (:require
    [clojure.string :as s]
    [clojure.java.io :as io]))

(defn clean-word
  [word]
  (-> word s/lower-case (s/replace #"\s" "_") keyword))

(defn load-data
  [resource]
  (->> resource io/resource slurp s/split-lines (map clean-word) (into [])))

(def languages (load-data "languages.txt"))

(def countries (load-data "countries.txt"))

(def interests (load-data "interests.txt"))

(defn ->advert
  [language country interests]
  {:language language :country country :interests (set interests)})

(defn generate-advert
  [languages countries interests]
  (->advert (rand-nth languages)
            (rand-nth countries)
            (take (inc (rand-int 5)) (shuffle interests))))

(defn generate
  [n]
  (into {}
    (map (fn [id ad] [id (assoc ad :id id)])
         (range 1 (inc n))
         (repeatedly n #(generate-advert languages countries interests)))))
