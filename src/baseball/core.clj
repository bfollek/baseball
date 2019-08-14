;;;; Adapted from
;;;; https://tht.fangraphs.com/baseball-coding-with-rust-intro/
;;;; https://tht.fangraphs.com/baseball-coding-with-rust-part-2/

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
      xml/parse-str
      :attrs
      (select-keys [:ampm
                    :away_league_id :away_team_city :away_team_id :away_team_name
                    :game_pk :game_type
                    :home_league_id :home_team_city :home_team_id :home_team_name
                    :time :time_zone :venue :venue_w_chan_loc])))

(defn- linescore-get
  [game]
  (-> game
      (str "linescore.xml")
      linescore-parse))

(defn- linescores
  "`f` should be `map` or `pmap`"
  [games f]
  (f linescore-get games))

(comment (def url (game-day-url "mlb" "2018" "06" "10")))
(comment (def games (game-day-links url)))
(comment (def ls (linescores games pmap)))