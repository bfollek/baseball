(ns baseball.core-test
  (:require [clojure.test :refer :all]
            [baseball.core :refer :all]))

(def ^{:private true} known-url "http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10")

(deftest game-day-url-test
  (testing "game-day-url"
    (let [f #'baseball.core/game-day-url]
      (is (= known-url (f "mlb" "2018" "06" "10"))))))

(deftest game-day-links-test
  (testing "game-day-links"
    (let [f #'baseball.core/game-day-links
          result (f known-url)]
      (is (= 15 (count result)))
      (is (= "http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10/gid_2018_06_10_anamlb_minmlb_1/" (first result)))
      (is (= "http://gd2.mlb.com/components/game/mlb/year_2018/month_06/day_10/gid_2018_06_10_slnmlb_cinmlb_1/" (last result))))))
