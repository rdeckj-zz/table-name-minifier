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
  ;TODO
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

(def commands {:help    {:label    "--help"
                         :pattern   "(--help)"
                         :function command-help}
               :verbose {:label    "--verbose"
                         :pattern  "(--verbose)"
                         :function command-verbose}})

(def command-labels (->> commands (map (fn [[_ command]] (:pattern command)))))

(defn -main
  "Take user input and process"
  [input]
  (condp str/includes? input
    ; run each command by matching on label
    (get-in commands [:verbose :label]) ((get-in commands [:help :function]))
    (get-in commands [:help :label]) ((get-in commands [:help :function])) ;; TODO pass input
    ; process input
    (-> input
        ; remove the commands from the input
        (str/replace (re-pattern (str/join "|" command-labels)) "")
        ; remove white spaces before processing
        (str/replace #"[\s]" "")
        shorten-table)))
