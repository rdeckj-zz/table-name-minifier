(ns table-name-minifier.core-test
  (:require [clojure.test :refer :all]
            [table-name-minifier.core :refer :all]))

(defn verbose-mode-fixture [test-fn]
  (reset! verbose false)
  (test-fn)
  (reset! verbose false))

(use-fixtures :each verbose-mode-fixture)

(deftest handle-commands-tests
  (testing "collection without commands present returns the same collection"
    (let [input ["some", "table", "name"]]
      (is (= (handle-commands input) input))))

  (testing "collection with commands present returns collection without commands"
    (is (= (handle-commands ["some", "--verbose", "name"]) ["some", "name"]))))

(deftest minify-special-words-tests
  (testing "percent returns pct"
    (is (= (minify-special-words ["percent"]) ["pct"])))

  (testing "state returns st"
    (is (= (minify-special-words ["state"]) ["st"])))

  (testing "pound returns lb"
    (is (= (minify-special-words ["pound"]) ["lb"])))

  (testing "not special word returns input"
    (is (= (minify-special-words ["potato"]) ["potato"])))

  (testing "single word"
    (is (= (minify-special-words ["pound"]) ["lb"])))

  (testing "multiple words"
    (is (= (minify-special-words ["pound", "state", "percent"]) ["lb", "st", "pct"])))

  (testing "returns normal words"
    (is (= (minify-special-words ["normal"]) ["normal"]))))

(deftest minify-input-tests-over-length-limit
  (testing "single word"
    (is (= (minify-input ["this"] 3) ["ths"])))

  (testing "multiple words"
    (is (= (minify-input ["this", "that", "other"] 10) ["ths", "tht", "thr"])))

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
  (testing "returns special words"
    (is (= (minify-input ["pound"] 32) ["lb"])))

  (testing "mixed input minifies special word"
    (is (= (minify-input ["pound", "potato"], 32) ["lb", "potato"])))

  (testing "normal words not minified"
    (is (= (minify-input ["this", "that"], 32) ["this", "that"]))))

(deftest strip-seperators-tests
  (testing "single word returns single item"
    (is (= (strip-separators "one") ["one"])))

  (testing "underscore seperator"
    (is (= (strip-separators "one_two") ["one", "two"])))

  (testing "space seperator"
    (is (= (strip-separators "one two") ["one", "two"]))))

(deftest reform-table-name-tests
  (testing "reforms single word"
    (is (= (reform-table-name ["potato"]) "potato")))

  (testing "reforms multiple words"
    (is (= (reform-table-name ["one", "two", "three"]), "one_two_three"))))

(deftest -main-tests
  (testing "main prints correct value"
    (is (= (with-out-str (-main "really_long_table_name_over_32_characters_and_it_sucks "))
           "rlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks\n")))

  (testing "verbose command prints extra stuff out"
    (is (= (with-out-str (-main "--verbose really_long_table_name_over_32_characters_and_it_sucks "))
           "... abbreviating special words\n... string still too long removing vowels\nrlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks\n"))))
