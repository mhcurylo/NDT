(ns ndt.go
  (:require 
    [datascript.core :as d]
    [cljs.core.async :as async]
    [ndt.routes :as r]
    [ndt.restapi :as api]
    [ndt.eb :as eb])
    (:require-macros
      [ndt.macros :refer [go-loop-sub]]
      [cljs.core.async.macros :refer [go go-loop]]))

(defn getinputvalue [db inputname]
  (:input/value (d/entity @db [:input/name inputname])))

(go-loop-sub eb/event-bus-pub :set-input-value [_ db inputname newval]
             (d/transact! db [{:input/name inputname
                               :input/value newval}]))

(go-loop-sub eb/event-bus-pub :submit-login-form [_ db]
             (let [[username password] [(getinputvalue db "loginform/name")
                                        (getinputvalue db "loginform/password")]]
               (if (api/correctuserdata username password)
                   (do (api/authenticateuser db true)
                       (println "win" (r/home))
                       (r/nav! "/"))
                   (api/authenticateuser db false))))
