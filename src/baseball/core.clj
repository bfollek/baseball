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

(defn- parse-xml
  [url]
  (-> url
      slurp
      xml/parse-str))

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
  [url] ; http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10/gid_2018_06_10_anamlb_minmlb_1/linescore.xml
  (-> url
      parse-xml
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

(defn- linescores-strategy
  "`f` should be `map` or `pmap`"
  [games f]
  (f linescore-get games))

(defn- linescores
  [games]
  (linescores-strategy games pmap))

(defn- boxscore-html
  "Drill down through the xml till we get the string of html."
  [url] ; http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10/gid_2018_06_10_anamlb_minmlb_1/boxscore.xml
  (->>
   ; Get a vector of maps
   (-> url
       parse-xml
       :content)
   ; Find the map that has {:tag :game_info}
   (filter #(= (:tag %) :game_info))
   first
   :content
   first))

(defn- boxscore-parse
  [url]
  (let [html (boxscore-html url)
        ;; I'm getting nowhere wit hickory.select, and the content strings would need parsing anyway, so...
        ;; <b>Weather</b>: 69 degrees, overcast.<br/>
        [_ weather-temp weather-condition] (re-find #"Weather.*\s+(\d+).*,\s+((\w|\s)+)\." html)
        ;; <b>Wind</b>: 14 mph, In from LF.<br/>
        [_ wind-speed wind-direction] (re-find #"Wind.*\s+(\d+).*,\s+((\w|\s)+)\." html)
        ;;  <b>Att</b>: 28,656.<br/>
        ;; Att is optional. It may not be in the html.
        ;; In that case, it'll end up as nil.
        [_ att1 att2] (re-find #"Att.*\s+(\d+),?(\d+)\." html)]
    {:attendance (str att1 att2)
     :weather-condition weather-condition :weather-temp weather-temp
     :wind-direction wind-direction :wind-speed wind-speed}))

(defn- boxscore-get
  [game]
  (-> game
      (str "boxscore.xml")
      boxscore-parse))

(defn- boxscores
  [games]
  (pmap boxscore-get games))

(comment (require :reload '[baseball.core :as bb]))
(comment (def url (game-day-url "mlb" "2018" "06" "10")))
(comment (def games (game-day-links url)))
(comment (def game (first games)))
(comment (boxscore-get game))
(comment (def bs (boxscores games)))
(comment (def ls (linescores games)))