(ns table-name-minifier.core-test
  (:require [clojure.test :refer :all]
            [table-name-minifier.core :refer :all]))

(deftest shorten-table-tests
  (testing "long table name minified"
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

(deftest command-help-tests
  (testing "prints help text"
    (is (= (clojure.string/trim-newline (with-out-str (command-help "--help")))
           "Usage: tnmin [--help] <command> [<args>]")))

  (testing "returns empty string"
    (is (= (command-help "--help some_input_text") ""))))

(deftest command-max-tests
  (testing "removes command and parameter"
    (is (= (command-max "--max 30 table_name") "table_name"))))

(deftest remove-command-tests
  (testing "command without parameters"
    (is (= (remove-command "--help" "--help" false) "")))

  (testing "command with parameters"
    (is (= (remove-command "--max 20 something" "--max" true) "something"))))

(deftest get-parameter-tests
  (testing "single command"
    (is (= (get-parameter "--max 30 table_name" "--max") "30")))

  (testing "multiple commands"
    (is (= (get-parameter "--something 50 --max 20 table_name" "--max") "20"))))

(deftest -main-tests
  (testing "long table name minified"
    (is (= (clojure.string/trim-newline (with-out-str (-main "really_long_table_name_over_32_characters_and_it_sucks")))
           "rlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks")))

  (testing "--max <num> modifies max length"
    (is (= (clojure.string/trim-newline (with-out-str (-main "--max 5 over_five")))
           "vr_fv"))))
