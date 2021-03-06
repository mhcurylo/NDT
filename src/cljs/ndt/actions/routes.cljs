(ns ndt.actions.routes
  (:require 
    [datascript.core :as d]
    [cljs.core.async :as async]
    [secretary.core :as secretary :refer-macros [defroute]]
    [ndt.data.datascript :as ds]
    [ndt.data.localstorage :as ls]
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
  (when-not (.-isNavigation e)
            (js/window.scrollTo 0 0))
  (if (or (ls/is-token-valid?) (= (get-token) (login))) 
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
  

