(ns table-name-minifier.core
  (:require [clojure.string :as str]))

(def commands ["--help"]
  )

(defn command-not-found
  "Print command not found error to user"
  [unknown-command]
  (print "tnmin: '")
  (print unknown-command)
  (print "' is not a tnmin command. See 'tnmin --help'.\n")
  (flush)
  )

(defn find-command
  "Search command list for valid command flags"
  [optional-args]
  (if (empty? optional-args)
    ()
    (some #(= optional-args %) commands))
  )

(defn remove-vowels
  "Remove vowels from the string"
  [input]
  (str/replace input #"[aeiou]" ""))

(defn minify-input
  "Condense the input string"
  [long-name]
  (remove-vowels long-name))

(defn -main
  "Take user input and process"
  [& args]
  (let [[first & optional-args] args]
    (if (find-command optional-args)
      ()
      (command-not-found optional-args))
    (println (minify-input first))
))