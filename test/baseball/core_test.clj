(ns baseball.core-test
  (:require [clojure.test :refer :all]
            [baseball.core :refer :all]
            [rabbithole.core :as rh]))

(def ^{:private true} known-url "http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10")
(def ^{:private true} linescores-url (str known-url "/gid_2018_06_10_anamlb_minmlb_1/linescore.xml"))

(deftest game-day-url-test
  (testing "game-day-url basics"
    (let [f #'baseball.core/game-day-url]
      (is (= known-url (f "mlb" "2018" "06" "10"))))))

(deftest game-day-links-test
  (testing "game-day-links basics"
    (let [f #'baseball.core/game-day-links
          result (sort (f known-url))]
      (is (= 15 (count result)))
      (is (= (str known-url "/gid_2018_06_10_anamlb_minmlb_1/") (first result)))
      (is (= (str known-url "/gid_2018_06_10_slnmlb_cinmlb_1/") (last result))))))

(deftest linescore-parse-test
  (testing "linescore-parse basics"
    (let [f #'baseball.core/linescore-parse
          result (f linescores-url)]
      (is (= "Twins" (:home_team_name result)))
      (is (= "Angels" (:away_team_name result))))))

(deftest linescores-test
  (testing "linescores basics"
    (let [f #'baseball.core/linescores
          venues (->> known-url
                      (#'baseball.core/game-day-links)
                      f
                      (map :venue))]
      (is (>= (rh/coll-index venues "Target Field") 0))
      (is (>= (rh/coll-index venues "Great American Ball Park") 0))
      (is (>= (rh/coll-index venues "Marlins Park") 0))
      (is (= (rh/coll-index venues "No Such Field") -1)))))
