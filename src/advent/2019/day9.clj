(ns advent.2019.day9
  (:require [advent.2019.intcode :as intcode]))

(def boost
  [1102 34463338 34463338 63 1007 63 34463338 63 1005 63 53 1101 0 3 1000 109 988 209 12 9 1000 209 6 209 3 203 0 1008 1000 1 63 1005 63 65 1008 1000 2 63 1005 63 904 1008 1000 0 63 1005 63 58 4 25 104 0 99 4 0 104 0 99 4 17 104 0 99 0 0 1102 1 32 1016 1102 326 1 1029 1102 1 26 1009 1102 1 753 1024 1102 1 1 1021 1102 35 1 1000 1102 1 0 1020 1101 25 0 1012 1102 36 1 1011 1101 0 33 1013 1102 1 667 1022 1102 1 38 1014 1102 1 24 1017 1101 0 31 1004 1102 443 1 1026 1101 37 0 1015 1101 27 0 1007 1101 0 748 1025 1102 1 23 1008 1102 1 34 1002 1101 28 0 1006 1102 1 22 1003 1101 0 29 1005 1101 0 39 1018 1101 21 0 1019 1102 30 1 1001 1102 660 1 1023 1102 1 331 1028 1101 0 440 1027 1101 0 20 1010 109 18 1206 2 195 4 187 1105 1 199 1001 64 1 64 1002 64 2 64 109 -12 1208 0 28 63 1005 63 217 4 205 1105 1 221 1001 64 1 64 1002 64 2 64 109 3 2101 0 -5 63 1008 63 31 63 1005 63 247 4 227 1001 64 1 64 1106 0 247 1002 64 2 64 109 -7 2101 0 6 63 1008 63 26 63 1005 63 267 1105 1 273 4 253 1001 64 1 64 1002 64 2 64 109 10 21108 40 40 4 1005 1016 295 4 279 1001 64 1 64 1106 0 295 1002 64 2 64 109 -9 2107 23 0 63 1005 63 315 1001 64 1 64 1105 1 317 4 301 1002 64 2 64 109 30 2106 0 -5 4 323 1105 1 335 1001 64 1 64 1002 64 2 64 109 -19 1202 -9 1 63 1008 63 26 63 1005 63 355 1106 0 361 4 341 1001 64 1 64 1002 64 2 64 109 -5 21107 41 42 6 1005 1015 379 4 367 1105 1 383 1001 64 1 64 1002 64 2 64 109 -6 21108 42 43 8 1005 1011 403 1001 64 1 64 1105 1 405 4 389 1002 64 2 64 109 11 21102 43 1 1 1008 1015 42 63 1005 63 425 1106 0 431 4 411 1001 64 1 64 1002 64 2 64 109 13 2106 0 0 1105 1 449 4 437 1001 64 1 64 1002 64 2 64 109 1 1205 -7 463 4 455 1106 0 467 1001 64 1 64 1002 64 2 64 109 -14 1206 7 479 1105 1 485 4 473 1001 64 1 64 1002 64 2 64 109 -6 1202 0 1 63 1008 63 23 63 1005 63 507 4 491 1106 0 511 1001 64 1 64 1002 64 2 64 109 13 1205 -1 523 1106 0 529 4 517 1001 64 1 64 1002 64 2 64 109 -23 2107 22 10 63 1005 63 551 4 535 1001 64 1 64 1106 0 551 1002 64 2 64 109 14 21101 44 0 6 1008 1018 44 63 1005 63 577 4 557 1001 64 1 64 1106 0 577 1002 64 2 64 109 -12 2108 32 0 63 1005 63 597 1001 64 1 64 1105 1 599 4 583 1002 64 2 64 109 7 1201 -4 0 63 1008 63 20 63 1005 63 619 1106 0 625 4 605 1001 64 1 64 1002 64 2 64 109 -11 1201 6 0 63 1008 63 34 63 1005 63 647 4 631 1106 0 651 1001 64 1 64 1002 64 2 64 109 20 2105 1 7 1001 64 1 64 1106 0 669 4 657 1002 64 2 64 109 -4 21101 45 0 6 1008 1018 46 63 1005 63 689 1106 0 695 4 675 1001 64 1 64 1002 64 2 64 109 -16 2108 22 7 63 1005 63 717 4 701 1001 64 1 64 1105 1 717 1002 64 2 64 109 10 1207 0 27 63 1005 63 733 1105 1 739 4 723 1001 64 1 64 1002 64 2 64 109 8 2105 1 10 4 745 1105 1 757 1001 64 1 64 1002 64 2 64 109 1 21102 46 1 -2 1008 1013 46 63 1005 63 779 4 763 1106 0 783 1001 64 1 64 1002 64 2 64 109 -2 1208 -7 29 63 1005 63 799 1105 1 805 4 789 1001 64 1 64 1002 64 2 64 109 -19 2102 1 10 63 1008 63 32 63 1005 63 829 1001 64 1 64 1106 0 831 4 811 1002 64 2 64 109 14 1207 -2 29 63 1005 63 849 4 837 1105 1 853 1001 64 1 64 1002 64 2 64 109 8 21107 47 46 -6 1005 1010 873 1001 64 1 64 1106 0 875 4 859 1002 64 2 64 109 -17 2102 1 6 63 1008 63 29 63 1005 63 901 4 881 1001 64 1 64 1106 0 901 4 64 99 21102 1 27 1 21102 1 915 0 1106 0 922 21201 1 27817 1 204 1 99 109 3 1207 -2 3 63 1005 63 964 21201 -2 -1 1 21101 0 942 0 1105 1 922 21202 1 1 -1 21201 -2 -3 1 21102 1 957 0 1105 1 922 22201 1 -1 -2 1106 0 968 22102 1 -2 -2 109 -3 2105 1 0])

(defn run []
  (intcode/execute-instructions :boost boost))