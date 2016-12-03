(ns ndt.view.entry
  (:require 
    [cljs.core.async :as async]
    [rum.core :as rum]
    [datascript.core :as d]
    [clojure.string :as string]
    [ndt.view.loginpage :as lp]))

(defn currentpage [db]
    (:app/page (d/entity @db [:app/title "ndt"])))

(rum/defc homepage [db bus]
     [:.row [:.three.columns]
            [:.six-columns [:h1 "Logged in, you are home."]]])

(rum/defc blankpage [db bus]
     [:div])

(rum/defc routerpage [db bus]
     (let [page (case (currentpage db) 
                  "loginpage" lp/loginpage
                  "homepage" homepage
                  blankpage)]
       (page db bus)))


(defn mountndt [page db bus]
  (rum/mount (page db bus)
           (. js/document (getElementById "app"))))

