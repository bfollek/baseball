;;;; Adapted from https://tht.fangraphs.com/baseball-coding-with-rust-intro/

(ns baseball.core
  (:require [clojure.string :as str]
            [hickory.core :as hi]
            [hickory.select :as his]))

(defn- game-day-url
  [level year month day]
  (str "http://gd2.mlb.com/components/game/" level "/year_" year "/month_" month "/day_" day))

(defn- parse-html
  [url]
  (-> url
      slurp
      hi/parse
      hi/as-hickory))

(defn- game-day-links
  [url]
  (->> url
       parse-html
       (his/select (his/descendant (his/tag :li) (his/tag :a)))
       (map :content)
       (map first) ; Pull string out of vector
       (filter #(str/includes? %1 "gid_"))
       (map str/trim)
       (map #(str url "/" %1))
       sort))
