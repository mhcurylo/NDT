(ns ndt.core
  (:require 
    [cljs.core.async :as async]
    [rum.core :as rum]
    [datascript.core :as d]
    [ndt.ui :as ui]
    [ndt.eb :as eb]
    [ndt.ds :as ds]))

(enable-console-print!)

(ui/mountndt ui/loginpage ds/conn eb/event-bus)

(d/listen! ds/conn
   (fn [tx-data]
     (ui/mountndt ui/loginpage ds/conn eb/event-bus)))

;;(defn fig-reload-hook []
;; ((ui/mountndt ui/loginpage ds/conn eb/event-bus))
