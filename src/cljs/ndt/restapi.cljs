(ns ndt.restapi
  (:require 
    [datascript.core :as d]))

(defn authenticateuser [db auth]
  (d/transact! db [{:app/title "ndt"
                    :app/auth auth}]))

(defn correctuserdata [admin password]
  (= [admin password] ["admin" "password"]))
