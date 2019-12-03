(ns advent.2019.day1)

(def module-masses
  [93912
   138996
   112824
   110011
   139024
   132292
   74029
   81664
   138077
   109614
   121056
   136338
   132771
   86611
   131526
   123101
   61315
   93900
   62070
   97957
   67168
   119464
   119066
   111076
   56856
   144203
   109400
   120187
   57915
   143353
   71308
   67695
   141275
   106552
   136209
   86990
   98969
   57207
   99103
   71940
   63145
   91765
   121095
   139700
   128851
   77138
   66712
   91318
   96924
   132235
   99897
   67479
   87996
   121100
   55411
   61715
   130658
   121030
   141445
   83939
   90402
   121107
   59618
   120112
   58140
   103514
   90538
   55552
   142739
   61770
   147374
   80038
   128830
   93328
   52369
   71801
   144536
   147140
   118213
   128056
   92155
   114384
   89234
   124451
   94214
   79174
   108427
   111041
   96715
   128414
   62521
   93897
   107428
   90637
   126176
   78676
   69504
   93663
   80869
   124230])

(defn mass->fuel* [mass]
  (-> mass
      (/ 3)
      (Math/floor)
      int
      (- 2)))

(defn mass->fuel [mass]
  (loop [fuel (mass->fuel* mass)
         total 0]
    (if (pos? fuel)
      (recur (mass->fuel* fuel) (+ total fuel))
      total)))

(defn run []
  (->> module-masses
       (map mass->fuel)
       (reduce + 0)))