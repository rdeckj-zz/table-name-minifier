(ns table-name-minifier.core
  (:require [clojure.string :as str])
  (:gen-class :main true))

(def max-length 32)

(defn command-help
  "Display the help screen"
  []
  (println "Usage: tnmin [--help] <command> [<args>]"))

(defn command-max
  "User specified max length"
  [input]
  (println "fuck")
  )

(def abbreviations
  {#"percent" "pct"
   #"state" "st"
   #"pound" "lb"
   #"miscellaneous" "misc"
   #"number" "num"
   #"temperature" "temp"
   #"department" "dept"})

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

(def commands {:help    {:label    "--help"
                         :pattern   "(--help)"
                         :function command-help}
               :max {:label    "--max"
                         :pattern  "(--max)"
                         :function command-max}})

(def command-labels (->> commands (map (fn [[_ command]] (:pattern command)))))

(defn -main
  "Take user input and process"
  [input]
  (condp str/includes? input
    ; run each command by matching on label
    (get-in commands [:max :label]) :>> (get-in commands [:max :function])
    (get-in commands [:help :label]) :>> (get-in commands [:help :function])
    ; process input
    (-> input
        ; remove the commands from the input
        (str/replace (re-pattern (str/join "|" command-labels)) "")
        ; remove white spaces before processing
        (str/replace #"[\s]" "")
        shorten-table
        println)))
