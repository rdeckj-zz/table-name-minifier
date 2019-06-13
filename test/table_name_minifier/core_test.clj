(ns table-name-minifier.core-test
  (:require [clojure.test :refer :all]
            [table-name-minifier.core :refer :all]))

(deftest handle-commands-tests
  (testing "collection without commands present returns the same collection"
    (let [input ["some", "table", "name"]]
      (is (= (handle-commands input) input))))

  (testing "collection with commands present returns collection without commands"
    (is (= (handle-commands ["some", "--verbose", "name"]) ["some", "name"]))))

(deftest process-special-words-tests
  (testing "percent returns pct"
    (is (= (process-special-words "percent") "pct")))

  (testing "state returns st"
    (is (= (process-special-words "state") "st")))

  (testing "pound returns lb"
    (is (= (process-special-words "pound") "lb")))

  (testing "not special word returns input"
    (is (= (process-special-words "potato") "potato"))))

(deftest minify-special-words-tests
  (testing "single word"
           (is (= (minify-special-words ["pound"]) ["lb"])))

  (testing "multiple words"
           (is (= (minify-special-words ["pound", "state", "percent"]) ["lb", "st", "pct"])))

  (testing "returns normal words"
           (is (= (minify-special-words ["normal"]) ["normal"]))))

(deftest minify-normal-words-tests
  (testing "single word"
           (is (= (minify-normal-words ["this"]) ["ths"])))

  (testing "multiple words"
           (is (= (minify-normal-words ["this", "that", "other"]) ["ths", "tht", "thr"])))

  (testing "returns special words"
           (is (= (minify-special-words ["pound"]) ["lb"]))))

(deftest minify-input-tests-over-length-limit
  (testing "single word normal input minified by removing vowels"
           (is (= (minify-input ["testing"], 5) ["tstng"])))

  (testing "multiple word normal input minified by removing vowels"
    (is (= (minify-input ["testing", "normal"], 8) ["tstng", "nrml"])))

  (testing "single word special input minified using special abbreviation"
    (is (= (minify-input ["pound"], 7) ["lb"])))

  (testing "multiple word special input minified by using special abbreviation"
    (is (= (minify-input ["pound", "state"], 5) ["lb", "st"])))

  (testing "mixed input minifies both"
           (is (= (minify-input ["normal", "pound"], 4) ["nrml", "lb"]))))

(deftest minify-input-tests-under-length-limit
  (testing "mixed input minifies special word"
           (is (= (minify-input ["pound", "potato"], 32) ["lb", "potato"])))

  (testing "normal words not minified"
           (is (= (minify-input ["this", "that"], 32) ["this", "that"]))))

(deftest strip-seperators-tests
  (testing "single word returns single item"
    (is (= (strip-separators "one") ["one"])))

  (testing "underscore seperator"
    (is (= (strip-separators "one_two") ["one", "two"]))))

(deftest reform-table-name-tests
  (testing "reforms single word"
    (is (= (reform-table-name ["potato"]) "potato")))

  (testing "reforms multiple words"
           (is (= (reform-table-name ["one", "two", "three"]), "one_two_three"))))