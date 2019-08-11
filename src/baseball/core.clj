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
    (his/select (his/descendant (his/tag :a)) hickory-fmt)))


; (def html "<a href=\"foo.php\">foo</a>")
; (def parsed (hi/parse html)) => Document
; (def as (hi/as-hickory parsed)) => map
; (def zipped (hiz/hickory-zip (hi/as-hickory parsed))) => vector


  ; (-> (s/select (s/child (s/class "subCalender") ; sic
  ;                        (s/tag :div)
  ;                        (s/id :raceDates)
  ;                        s/first-child
  ;                        (s/tag :b))
  ;               site-htree)
  ;     first :content first string/trim)
