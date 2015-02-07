(in-ns 'help-notice.handler)

(defn range-size
  ([start size] (range-size start size 1))
  ([start size step]
    {:pre[(integer? size)]}
    (take size (range start (+ start (* size step)) step))))

(defn rand-str
  ([n] (rand-str n (mapcat #(apply range-size %) [[48 10] [65 26] [97 26]])))
  ([n charseq] (apply str (map char (repeatedly n #(rand-nth charseq))))))
