(ns ndt.routes
  (:require 
    [datascript.core :as d]
    [cljs.core.async :as async]
    [secretary.core :as secretary :refer-macros [defroute]]
    [ndt.ds :as ds]
    [goog.events])
  (:import [goog.history Html5History EventType])) 

(defn navigatedbpage [pagename]
  (d/transact! ds/conn [{:app/title "ndt"
                         :app/page pagename}]))

(defn get-token []
    (str js/window.location.pathname js/window.location.search))

(defn make-history []
  (doto (Html5History.)
    (.setPathPrefix (str js/window.location.protocol "//" js/window.location.host))
              (.setUseFragment false)))

(defroute login "/login" []
  (navigatedbpage "loginpage"))

(defroute logout "/logout" []
  (navigatedbpage "logoutpage"))

(defroute home "/" []
  (navigatedbpage "homepage"))

(defroute anywhere "*" []
  (navigatedbpage "homepage"))

(defn handle-url-change [e h]
  (js/console.log (login) (get-token))
  (when-not (.-isNavigation e)
            (js/window.scrollTo 0 0))
  (if (or (:app/auth (d/entity @ds/conn [:app/title "ndt"])) (= (get-token) (login))) 
    (secretary/dispatch! (get-token))
    (.setToken h (login))))

(defonce history (let [h (make-history)]
  (doto h
    (goog.events/listen EventType.NAVIGATE #(handle-url-change % h))
    (.setUseFragment false)
    (.setPathPrefix "")
    (.setEnabled true))))

(defn nav! [token]
    (.setToken history token))
  

