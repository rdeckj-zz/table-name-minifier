(ns table-name-minifier.core-test
  (:require [clojure.test :refer :all]
            [table-name-minifier.core :refer :all]))

(deftest -main-tests
  (testing "main prints correct value"
    (is (= (with-out-str (-main "really_long_table_name_over_32_characters_and_it_sucks "))
           "rlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks\n")))

  (testing "main prints correct value when passed command"
    (is (= (with-out-str (-main "--verbose really_long_table_name_over_32_characters_and_it_sucks "))
           "rlly_lng_tbl_nm_vr_32_chrctrs_nd_t_scks\n"))))
