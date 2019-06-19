(ns table-name-minifier.core-test
  (:require [clojure.test :refer :all]
            [table-name-minifier.core :refer :all]))

(deftest shorten-table-tests
  (testing "short table name not minified"
    (is (= (clojure.string/trim-newline (shorten-table "really_long_table_name_with_only_normal_words"))
           "rlly_lng_tbl_nm_wth_nly_nrml_wrds")))

  (testing "short table name not minified"
    (is (= (clojure.string/trim-newline (shorten-table "short_table_name"))
           "short_table_name"))))

(deftest replace-abbr-tests
  (testing "single word"
    (is (= (replace-abbr "pound") "lb")))

  (testing "abbreviation word at end"
    (is (= (replace-abbr "some_percent") "some_pct")))

  (testing "abbreviation word at start"
    (is (= (replace-abbr "state_test") "st_test")))

  (testing "abbreviation word in middle"
    (is (= (replace-abbr "something_state_test") "something_st_test")))

  (testing "no abbreviations returns input"
    (is (= (replace-abbr "no_abbreviations") "no_abbreviations"))))

(deftest -main-tests
  (testing "main prints correct value"
    (is (= (clojure.string/trim-newline (with-out-str (-main "really_long_table_name_over_32_characters_and_it_sucks")))
           "rlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks")))

  (testing "main prints correct value when passed command"
    (is (= (clojure.string/trim-newline (with-out-str (-main "--verbose really_long_table_name_over_32_characters_and_it_sucks")))
           "rlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks"))))
