(ns table-name-minifier.core
  (:require [clojure.string :as str]))

(defn command-help
  "Display the help screen"
  []
  (println "Usage: tnmin [--help] <command> [<args>]")
  (System/exit 0)
)

(defn command-verbose
  "Enable verbose mode"
  []
  ;TODO
)

(def commands [{:name "--help" :function command-help}
               {:name "--verbose" :function command-verbose}])

(def abbreviations [{:word "percent" :abbreviation "pct"}
                    {:word "street" :abbreviation "st"}
                    {:word "avenue" :abbreviation "ave"}])

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

(defn minify-input
  "Condense the input collection"
  [input]
  (loop [initial (first input)
         remaining (rest input)
         result (str "")]


    ;(if (= initial (process-special-words initial))
    ;  (remove-vowels initial)
    ;  (process-special-words initial)
    ;))


    (if-not (empty? remaining)
      (recur
        (first remaining)
        (rest remaining)
        (concat result
              (if (= initial (process-special-words initial))
                (remove-vowels initial)
                (process-special-words initial)
              )
        )
      )
      (if (= initial (process-special-words initial))
         (remove-vowels initial)
         (process-special-words initial)
         )
    )

  )
)

(defn append-command
  ""
  [bool not-commands current]
  (if-not bool
    (conj not-commands current)
    not-commands)
  )

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
        (reverse (append-command command-fn not-command initial))
        )
      )
    )
  )

(defn -main
  "Take user input and process"
  [& args]
  (let [input-col (str/split args #"[_ ]")]
    (if (handle-commands input-col)
    (do
      (let [result (minify-input input-col)]
        (println result)
        result)
      ))
  ))