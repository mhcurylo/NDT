(ns ndt.actions.events
  (:require 
    [datascript.core :as d]
    [cljs.core.async :as async]
    [ndt.actions.routes :as r]
    [ndt.remote.restapi :as api]
    [ndt.actions.eventbus :as eb])
    (:require-macros
      [ndt.core :refer [go-loop-sub]]
      [cljs.core.async.macros :refer [go go-loop]]))

(defn setinputvalue [db inputname newval]
  (d/transact! db [{:input/name inputname
                    :input/value newval}]))

(defn authenticateuser [db auth]
  (d/transact! db [{:app/title "ndt"
                    :app/auth auth}]))

(defn getinputvalue [db inputname]
  (:input/value (d/entity @db [:input/name inputname])))

(defn errormsg! [db errorname errormsg]
  (d/transact! db [{:error/name errorname
                    :error/msg errormsg}]))

(go-loop-sub eb/event-bus-pub :set-input-value [_ db inputname newval]
             (setinputvalue db inputname newval))

(go-loop-sub eb/event-bus-pub :submit-login-form [_ db]
             (authenticateuser db false)
             (let [[username password] [(getinputvalue db "loginform/name")
                                        (getinputvalue db "loginform/password")]]
               (if (async/<! (api/login username password))
                   (do (authenticateuser db true)
                       (setinputvalue db "loginform/name" "")
                       (setinputvalue db "loginform/password" "")
                       (errormsg! db "loginpage/error" "")
                       (r/nav! "/"))
                   (do (errormsg! db "loginpage/error" "Wrong user credintials.")))))
