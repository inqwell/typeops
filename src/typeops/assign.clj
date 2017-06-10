(ns typeops.assign
  (:refer-clojure :exclude [assoc merge])
  (:require [typeops.core :as t]))

(def ^:no-doc core-assoc clojure.core/assoc)

(def ^:dynamic *warn-on-absent-key*
  "When 'assigning' to a map key that is absent, print a warning.
  Defaults to true."
  true)

(defn- warn-on-absent-key!
  "Look for a value to base an assignment on. If the
  meta data contains a prototype then use that, otherwise
  use any existing value. Optionally issue a warning
  if the key is absent."
  [map key]
  (if-not (contains? map key)
    (if *warn-on-absent-key*
      (println (str "WARNING: absent key " key))))
  (or (-> map
          meta
          :proto
          (get key))
      (get map key)))

(defn assign
  "'assigns' val to the key within map. If the meta data
  is a map containing the key :proto the corresponding value
  will be used to align the type of val, with rounding or
  truncation as necessary. If there is no meta data any existing
  value is used to maintain the correct type/precision."
  ([map key val]
   (let [cur (warn-on-absent-key! map key)]
     (core-assoc map key (t/op-assign cur val))))
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

