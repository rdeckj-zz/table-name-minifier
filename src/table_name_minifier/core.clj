(ns table-name-minifier.core
  (:require [clojure.string :as str])
  (:gen-class :main true))

(def max-length 32)

(defn remove-command
  "Remove the specified command and it's parameters from the string"
  [input command params]
  (let [regex (if params
                (str command " [0-9]+ ")
                command)]
    (str/replace input (re-pattern regex) "")))

(defn get-parameter
  "Get the parameter value for the specified command"
  [input command]
  (let [split-input (str/split input #" ")]
    (get split-input (+ (.indexOf split-input command) 1))
    ))

(defn command-help
  "Display the help screen"
  [input]
  (println "Usage: tnmin [--help] <command> [<args>]")
  ; short-circuit program by passing back empty string
  "")

(defn command-max
  "User specified max length"
  [input]
  (def max-length (Integer. (get-parameter input "--max")))
  (remove-command input "--max" true)
  )

(def abbreviations
  {#"percent"       "pct"
   #"state"         "st"
   #"pound"         "lb"
   #"miscellaneous" "misc"
   #"number"        "num"
   #"temperature"   "temp"
   #"department"    "dept"})

(defn replace-abbr
  [table-name]
  (reduce (fn [s [regex abbr]] (str/replace s regex abbr))
          table-name
          abbreviations))

(defn remove-vowels
  "Remove vowels from the string"
  [input]
  (str/replace input #"[aeiou]" ""))

(defn shorten-table
  [input]
  (let [abbreviated-input (replace-abbr input)]
    (if (> (count abbreviated-input)
           max-length)
      (remove-vowels abbreviated-input)
      abbreviated-input
      )))

(defn -main
  "Take user input and process"
  [input]
  (cond-> input
          ; run each command by matching on label
          (str/includes? input "--max") command-max
          (str/includes? input "--help") command-help
          ; remove white spaces before processing
          true (str/replace #"[\s]" "")
          true shorten-table
          true println))
