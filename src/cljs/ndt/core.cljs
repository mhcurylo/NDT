(ns ndt.core
  (:require 
    [cljs.core.async :as async]
    [rum.core :as rum]
    [datascript.core :as d]
    [ndt.ui :as ui]
    [ndt.eb :as eb]
    [ndt.ds :as ds]
    [ndt.go :as go]
    [ndt.routes :as routes]))

(enable-console-print!)

(ui/mountndt ui/routerpage ds/conn eb/event-bus)

(d/listen! ds/conn
   (fn [tx-data]
     (ui/mountndt ui/routerpage ds/conn eb/event-bus)))
