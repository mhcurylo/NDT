(ns ndt.eb
  (:require 
    [datascript.core :as d]
    [cljs.core.async :as async])
    (:require-macros
      [ndt.macros :refer [go-loop-sub]]
      [cljs.core.async.macros :refer [go go-loop]]))

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

(go-loop-sub event-bus-pub :set-input-value [_ db inputname newval]
             (d/transact! db [{:input/name inputname
                               :input/value newval}]))
