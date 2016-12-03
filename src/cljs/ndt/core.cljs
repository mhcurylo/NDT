(ns ndt.core
  (:require 
    [cljs.core.async :as async]
    [rum.core :as rum]
    [datascript.core :as d]
    [ndt.view.entry :as v]
    [ndt.data.datascript :as ds]
    [ndt.actions.eventbus :as eb]
    [ndt.actions.events :as e]
    [ndt.actions.routes :as routes]))

(enable-console-print!)

(v/mountndt v/routerpage ds/conn eb/event-bus)

(d/listen! ds/conn
   (fn [tx-data]
     (v/mountndt v/routerpage ds/conn eb/event-bus)))
