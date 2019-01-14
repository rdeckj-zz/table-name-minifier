(ns table-name-minifier.core
  (:require [clojure.string :as str]))

(defn command-help
  "Display the help screen"
  []
  (println "Usage: tnmin [--help] <command> [<args>]")
  )

(def commands [{:name "--help" :function command-help}])
(def abbreviations [{:word "percent" :abbreviation "pct"}
                    {:word "street" :abbreviation "st"}
                    {:word "avenue" :abbreviation "ave"}])

(defn get-command-fn
  "Search command list for valid command flags"
  [optional-args]
  (when-not (empty? optional-args)
    (:function (first (filter #(= optional-args (:name %)) commands)))))

(defn command-not-found
  "Print command not found error to user"
  [unknown-command]
  (print "tnmin: '")
  (print unknown-command)
  (print "' is not a tnmin command. See 'tnmin --help'.\n")
  (flush)
  )

(defn remove-vowels
  "Remove vowels from the string"
  [input]
  (str/replace input #"[aeiou]" ""))

(defn process-special-words
  ""
  [word]
  (let [result (:abbreviation (first (filter #(= word (:word %)) abbreviations)))]
    (if-not (empty? result)
      (str result)
      (str word))))

(defn minify-input
  "Condense the input string"
  [long-name]
  (remove-vowels (process-special-words long-name)))

(defn handle-commands
  "Handle special commands"
  [possible-command]
  (let [command-fn (get-command-fn possible-command)]
    (if command-fn
      (command-fn)
      ())))

(defn -main
  "Take user input and process"
  [& args]
  (let [[first-arg & remaining-args] args]
    (if (handle-commands first-arg)
      (do
        (let [result (minify-input first-arg)]
          (println result)
          result))
      ())
    ))