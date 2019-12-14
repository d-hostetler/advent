(ns advent.2019.day11
  (:require [advent.2019.intcode :as intcode]
            [clojure.core.async :as async]))

(def rotate-left
  {:down :right
   :left :down
   :right :up
   :up :left})

(def rotate-right
  {:down :left
   :left :up
   :right :down
   :up :right})

(def memory
  [3 8 1005 8 311 1106 0 11 0 0 0 104 1 104 0 3 8 102 -1 8 10 1001 10 1 10 4 10 108 0 8 10 4 10 1002 8 1 28 1006 0 2 2 109 10 10 1 1 19 10 1 1103 20 10 3 8 102 -1 8 10 1001 10 1 10 4 10 108 1 8 10 4 10 1002 8 1 65 1006 0 33 1 7 0 10 3 8 102 -1 8 10 101 1 10 10 4 10 108 0 8 10 4 10 1002 8 1 94 3 8 102 -1 8 10 1001 10 1 10 4 10 108 1 8 10 4 10 101 0 8 116 1 1002 1 10 3 8 1002 8 -1 10 1001 10 1 10 4 10 108 0 8 10 4 10 1002 8 1 142 2 1101 6 10 3 8 1002 8 -1 10 101 1 10 10 4 10 108 0 8 10 4 10 1001 8 0 168 2 1107 7 10 1006 0 68 1 5 6 10 1 2 5 10 3 8 1002 8 -1 10 1001 10 1 10 4 10 1008 8 0 10 4 10 1002 8 1 206 1 1008 16 10 3 8 102 -1 8 10 1001 10 1 10 4 10 1008 8 1 10 4 10 1001 8 0 232 3 8 102 -1 8 10 101 1 10 10 4 10 108 1 8 10 4 10 102 1 8 253 1006 0 30 2 1 4 10 1 1008 1 10 2 1109 4 10 3 8 102 -1 8 10 1001 10 1 10 4 10 1008 8 1 10 4 10 102 1 8 291 101 1 9 9 1007 9 1051 10 1005 10 15 99 109 633 104 0 104 1 21102 387508339604 1 1 21102 1 328 0 1106 0 432 21101 0 47677022988 1 21101 0 339 0 1106 0 432 3 10 104 0 104 1 3 10 104 0 104 0 3 10 104 0 104 1 3 10 104 0 104 1 3 10 104 0 104 0 3 10 104 0 104 1 21102 209382822080 1 1 21102 386 1 0 1105 1 432 21101 179318123523 0 1 21102 1 397 0 1105 1 432 3 10 104 0 104 0 3 10 104 0 104 0 21102 709584904960 1 1 21101 420 0 0 1106 0 432 21102 709580444008 1 1 21102 431 1 0 1105 1 432 99 109 2 21202 -1 1 1 21102 1 40 2 21101 0 463 3 21101 0 453 0 1105 1 496 109 -2 2105 1 0 0 1 0 0 1 109 2 3 10 204 -1 1001 458 459 474 4 0 1001 458 1 458 108 4 458 10 1006 10 490 1101 0 0 458 109 -2 2106 0 0 0 109 4 2102 1 -1 495 1207 -3 0 10 1006 10 513 21102 1 0 -3 21202 -3 1 1 22102 1 -2 2 21102 1 1 3 21102 532 1 0 1106 0 537 109 -4 2105 1 0 109 5 1207 -3 1 10 1006 10 560 2207 -4 -2 10 1006 10 560 21201 -4 0 -4 1106 0 628 22101 0 -4 1 21201 -3 -1 2 21202 -2 2 3 21101 579 0 0 1105 1 537 21201 1 0 -4 21101 1 0 -1 2207 -4 -2 10 1006 10 598 21102 0 1 -1 22202 -2 -1 -2 2107 0 -3 10 1006 10 620 21201 -1 0 1 21101 0 620 0 106 0 495 21202 -2 -1 -2 22201 -4 -2 -4 109 -5 2105 1 0])

(defn turn [direction rotate]
  (case rotate
    0
    (get rotate-left direction)
    1
    (get rotate-right direction)))

(defn forward [at direction]
  (case direction
    :down
    (update at 1 dec)
    :left
    (update at 0 dec)
    :right
    (update at 0 inc)
    :up
    (update at 1 inc)))

(defn paint [in-chan out-chan]
  (loop [panels {[0 0] 1}
         at [0 0]
         direction :up]
    (async/>!! in-chan (get panels at 0))
    (if-let [color (async/<!! out-chan)]
      (let [rotate (async/<!! out-chan)
            new-direction (turn direction rotate)
            new-at (forward at new-direction)]
        (recur (assoc panels at color)
               new-at
               new-direction))
      panels)))

(defn draw [panels]
  (doseq [y (range -50 50)]
    (doseq [x (range -50 50)
            :let [color (get panels [y x] 0)]]
      (case color
        0
        (print " ")
        1
        (print "@")))
    (println)))

(defn run []
  (let [in-chan (async/chan 1)
        out-chan (async/chan)
        panels (future (paint in-chan out-chan))]
    (intcode/execute-instructions :painter
                                  memory
                                  (intcode/chan->in in-chan)
                                  (intcode/chan->out out-chan)
                                  (intcode/halt-chans out-chan))
    (draw @panels)))
