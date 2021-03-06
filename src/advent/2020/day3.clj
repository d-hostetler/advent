(ns advent.2020.day3
  (:require [clojure.java.io :as io]))

(defn parse-terrain []
  (->> "resources/2020/day3.input"
       io/reader
       line-seq
       (mapv vec)))

(defn next-x [x slope-x max-x]
  (let [next (+ x slope-x)]
    (if (> next max-x)
      (- next max-x 1)
      next)))

(defn next-y [y slope-y]
  (+ y slope-y))

(defn all-slope-coords [terrain slope-x slope-y]
  (let [max-x (dec (count (nth terrain 0)))
        max-y (dec (count terrain))]
    (loop [x 0
           y 0
           all-coords []]
      (if (<= y max-y)
        (recur (next-x x slope-x max-x)
               (next-y y slope-y)
               (conj all-coords [x y]))
        all-coords))))

(defn tree? [terrain [x y]]
  (= (-> terrain
         (nth y)
         (nth x))
     \#))

(defn count-trees [terrain [slope-x slope-y]]
  (->> (all-slope-coords terrain slope-x slope-y)
       (map (partial tree? terrain))
       (filter identity)
       count))

(defn run []
  (let [terrain (parse-terrain)
        slopes [[1 1]
                [3 1]
                [5 1]
                [7 1]
                [1 2]]]
    (->> slopes
         (map (partial count-trees terrain))
         (reduce *))))
