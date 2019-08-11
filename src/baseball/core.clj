;;;; Adapted from https://tht.fangraphs.com/baseball-coding-with-rust-intro/

(ns baseball.core
  (:require [clojure.string :as str]
            [hickory.core :as hi]
            [hickory.select :as his]
            [hickory.zip :as hiz]))

(defn game-day-url
  [level year month day]
  (str "http://gd2.mlb.com/components/game/" level "/year_" year "/month_" month "/day_" day))
(comment (slurp (game-day-url "mlb" "2018" "06" "10")))

(defn game-day-links
  [url]
  (let [hickory-fmt (-> url
                        slurp
                        hi/parse
                        hi/as-hickory)]
    (->> hickory-fmt
         (his/select (his/descendant (his/tag :a)))
         (map (comp first :content))
         (filter #(str/includes? %1 "gid_"))
         (map (comp #(str url "/" %1) str/trim)))))
