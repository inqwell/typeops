(ns typeops.assign
  (:refer-clojure :exclude [assoc merge])
  (:require [typeops.core :as t]))

(def ^:no-doc core-assoc clojure.core/assoc)

(def ^:dynamic *warn-on-absent-key*
  "May be bound to a function of two arguments. When 'assigning' to
  a map key that is absent the function is called passing the map
  and the key being applied."
  nil)

(defn- warn-on-absent-key!
  "Look for a value to base an assignment on. If the
  meta data contains a prototype then use that, otherwise
  use any existing value. Optionally call a function
  if the key is absent."
  [m k]
  (when (and *warn-on-absent-key* (not (contains? m k)))
    (*warn-on-absent-key* m k))
  (or (-> m
          meta
          :proto
          (get k))
      (get m k)))

(defn assign
  "'assigns' val to the key within map. If the meta data
  is a map containing the key :proto the corresponding value
  will be used to align the type of val, with rounding or
  truncation as necessary. If there is no meta data any existing
  value is used to maintain the correct type/precision."
  ([map key val]
   (let [cur (warn-on-absent-key! map key)]
     (if (true? t/*debug*)
       (binding [t/*debug* {:map map :key key :val val :cur cur}]
         (core-assoc map key (t/op-assign cur val)))
       (core-assoc map key (t/op-assign cur val)))))
  ([map key val & kvs]
   (let [ret (assign map key val)]
     (if kvs
       (if (next kvs)
         (recur ret (first kvs) (second kvs) (nnext kvs))
         (throw (IllegalArgumentException.
                  "assign expects even number of arguments after map/vector, found odd number")))
       ret))))

(def assoc
  "An alias for assign"
  assign)

(defn merge
  "Returns a map that consists of the rest of the maps conj-ed on to
  the first, using assign semantics.  If a key occurs in more than
  one map, the mapping from the latter (left-to-right) will be the
  mapping in the result."
  [& maps]
  (when (some identity maps)
    (reduce (fn [to from]
              (let [args (apply concat from)]
                (if (seq args)
                  (apply assign to args)
                  to)))
            maps)))

