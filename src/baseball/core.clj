;;;; Adapted from https://tht.fangraphs.com/baseball-coding-with-rust-intro/

(ns baseball.core
  (:require [clojure.string :as str]
            [clojure.data.xml :as xml]
            [hickory.core :as hic]
            [hickory.select :as his]))

(defn- game-day-url
  [level year month day]
  (str "http://gd2.mlb.com/components/game/" level "/year_" year "/month_" month "/day_" day))

(defn- parse-html
  [url]
  (-> url
      slurp
      hic/parse
      hic/as-hickory))

(defn- game-day-links
  [url]
  (->> url
       parse-html
       (his/select (his/descendant (his/tag :li) (his/tag :a)))
       (map :content)
       (map first) ; Pull string out of vector
       (filter #(str/includes? %1 "gid_"))
       (map str/trim)
       (map #(str url "/" %1))))

(defn- linescore-parse
  ;; http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10/gid_2018_06_10_anamlb_minmlb_1/linescore.xml
  [url]
  (-> url
      slurp
      (str/replace "&" "&amp;") ; Fix invalid ampersands
      xml/parse-str))