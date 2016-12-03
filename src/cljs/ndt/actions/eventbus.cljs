(ns ndt.actions.eventbus
  (:require 
    [cljs.core.async :as async]))

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))
