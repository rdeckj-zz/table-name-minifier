(ns table-name-minifier.core
  (:require [clojure.string :as str]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:gen-class :main true))

(def max-length 32)
(def use-abbreviations true)

(defn remove-command
  "Remove the specified command and it's parameters from the string"
  [input command params]
  (let [regex (if params
                (str command " [0-9]+ ")
                (str command " ?"))]
    (str/replace input (re-pattern regex) "")))

(defn get-parameter
  "Get the parameter value for the specified command"
  [input command]
  (let [split-input (str/split input #" ")]
    (get split-input (+ (.indexOf split-input command) 1))
    ))

(defn read-abbreviations-file
  [file-name]
  (if (.exists (io/as-file file-name))
    (with-open [reader (io/reader file-name)]
      (doall
        (csv/read-csv reader)))
    ""
    ))

(defn strip-abbreviations-comments
  "Remove lines that start with #"
  []
  (remove #(= \# (get (get % 0) 0)) (read-abbreviations-file ".abbreviations.csv")))

(defn command-help
  "Display the help screen"
  [input]
  (println "Usage: tnmin [arguments]")
  (println "Valid arguments are:")
  (println "")
  (println "--help                   Print help and exit")
  (println "--max <number>           Set the max table length")
  (println "--noabbr                 Treat words in .abbreviations like normal words")
  ; short-circuit program by passing back empty string
  "")

(defn command-max
  "User specified max length"
  [input]
  (def max-length (Integer. (get-parameter input "--max")))
  (remove-command input "--max" true)
  )

(defn command-noabbr
  "Ignore abbreviation words"
  [input]
  (def use-abbreviations false)
  (remove-command input "--noabbr" false)
  )

(defn replace-abbr
  [table-name]
  (reduce (fn [s [regex abbr]] (str/replace s regex abbr))
          table-name
          (strip-abbreviations-comments)))

(defn remove-vowels
  "Remove vowels from the string"
  [input]
  (str/replace input #"[aeiou]" ""))

(defn shorten-table
  [input]
  (let [abbreviated-input
        (if use-abbreviations
          (replace-abbr input)
          input)]
    (if (> (count abbreviated-input)
           max-length)
      (remove-vowels abbreviated-input)
      abbreviated-input
      )))

(defn -main
  "Take user input and process"
  ([] (command-help ""))
  ([input]
   (cond-> input
           ; run each command by matching on label
           (str/includes? input "--max") command-max
           (str/includes? input "--help") command-help
           (str/includes? input "--noabbr") command-noabbr
           ; remove white spaces before processing
           true (str/replace #"[\s]" "")
           true shorten-table
           true println)))
