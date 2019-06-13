(ns table-name-minifier.core
  (:require [clojure.string :as str])
  (:gen-class :main true))

(defn command-help
  "Display the help screen"
  []
  (println "Usage: tnmin [--help] <command> [<args>]")
  ;; (System/exit 0) ;; TODO this crashes the repl
  )

(defn command-verbose
  "Enable verbose mode"
  []
  ;TODO
  )

(def commands [{:name "--help" :function command-help}
               {:name "--verbose" :function command-verbose}])

(def abbreviations [{:word "percent" :abbreviation "pct"}
                    {:word "state" :abbreviation "st"}
                    {:word "pound" :abbreviation "lb"}])

(def default-max-length 32)

(defn get-command-fn
  "Search command list for valid command flags"
  [optional-args]
  (when-not (empty? optional-args)
    (:function (first (filter #(= optional-args (:name %)) commands)))))

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

(defn minify-special-words
  [input]
  (loop [current (first input)
         remaining (rest input)
         result []]

    (if-not (empty? current)
      (recur
       (first remaining)
       (rest remaining)
       (conj result
             (if (= current (process-special-words current))
               current
               (process-special-words current))))

      result)))

(defn minify-normal-words
  [input]
  (loop [current (first input)
         remaining (rest input)
         result []]

    (if-not (empty? current)
      (recur
       (first remaining)
       (rest remaining)
       (conj result
             (if (= current (process-special-words current))
               (remove-vowels current)
               current)))

      result)))

(defn append-command
  ""
  [bool not-commands current]
  (if-not bool
    (conj not-commands current)
    not-commands))

(defn handle-commands
  "Handle special commands"
  [input]
  (loop [initial (first input)
         remaining (rest input)
         not-command `()]

    (let [command-fn (get-command-fn initial)]
      (if command-fn
        (command-fn))

      (if-not (empty? remaining)
        (recur
         (first remaining)
         (rest remaining)
         (append-command command-fn not-command initial))
        (reverse (append-command command-fn not-command initial))))))

(defn strip-separators
  "Remove word separators"
  [input]
  (str/split input #"[_]"))

(defn reform-table-name
  "Reform table name"
  [input]
  (clojure.string/join "_" input))

(defn minify-input
  "Condense the input collection into a reduced collection"
  [input max-length]
  (let [first-pass (minify-special-words input)]
    (if (> (count (reform-table-name first-pass)) max-length)
      (minify-normal-words first-pass)
      first-pass)))

(defn -main
  "Take user input and process"
  [input]
  (println (reform-table-name (minify-input (handle-commands (strip-separators input)) default-max-length))))
