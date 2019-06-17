(ns table-name-minifier.core
  (:require [clojure.string :as str])
  (:gen-class :main true))

(defn command-help
  "Display the help screen"
  []
  (println "Usage: tnmin [--help] <command> [<args>]")
  (System/exit 0))

(defn command-verbose
  "Enable verbose mode"
  ;;TODO
  [])

(def max-length 32)

(def abbreviations
  {#"percent" "pct"
   #"state" "st"
   #"pound" "lb"})

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
  (cond-> (replace-abbr input)
    #(> (count %) max-length) remove-vowels
    :always                   println))

(defn -main
  "Take user input and process"
  [input]
  (condp str/includes? input
     "--help" (command-help)
     "--verbose" (command-verbose) ;;TODO pass input
     (shorten-table input)))
