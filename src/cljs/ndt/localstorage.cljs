(ns ndt.ls
  (:require [goog.crypt.base64 :as b64]
            [clojure.string :as str]))


(defn token! [token]
  (.setItem (.-localStorage js/window) "jws" token))


(defn token []
  (.getItem (.-localStorage js/window) "jws"))

(defn token-info []
  (let [tokstr (b64/decodeString (second (str/split (token) #"\.")))]
    (js/JSON.parse tokstr)))

(defn is-token-valid? []
  (let [tk (* 1000 (.-exp (token-info)))]
    (js/console.log tk (.getTime (js/Date.)))
    (> tk (.getTime (js/Date.)))))

(defn token-rm! []
  (.removeItem (.-localStorage js/window) "jws"))
