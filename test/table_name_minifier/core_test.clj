(ns table-name-minifier.core-test
  (:require [clojure.test :refer :all]
            [table-name-minifier.core :refer :all]))

(deftest basic-input
  (testing "basic input minifies by removing vowels"
    (is (= (-main "testing") "tstng"))))

(deftest help-command
  (testing "help command returns nothing"
    (is (= (-main "--help") ()))))

(deftest help-input
  (testing "help word returns minified version"
    (is (= (-main "help") "hlp"))))

(deftest special-word-percent
  (testing "percent becomes pct"
    (is (= (-main "percent") "pct"))))

;(deftest special-word-road
;  (testing "avenue becomes ave"
;    (is (= (-main "avenue") "ave"))))