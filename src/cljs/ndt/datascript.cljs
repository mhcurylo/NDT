(ns ndt.ds
  (:require 
    [datascript.core :as d]))

;; setting up database with schemas
(def conn (d/create-conn {
  :app/title {:db/unique :db.unique/identity}
  :app/page  {}
  :app/auth {}
  :form/name {:db/unique :db.unique/identity}
  :form/inputs {:db/cardinality :db.cardinality/many
                :db/type :db.type/ref}
  :input/name {:db/unique :db.unique/identity}
  :input/label {}
  :input/placeholder {}
  :input/value {}
  :input/type {}
  :input/required {}
  :input/disabled {}
  :error/name {:db/unique :db.unique/identity}
  :error/msg {} 
}))

;;initiating database with default values / needed data
(d/transact! conn [{:input/name "loginform/name"
                    :input/label "Name"
                    :input/placeholder "Enter your name"
                    :input/required true
                    :input/value ""
                    :input/type "text"}
                   {:input/name "loginform/password"
                    :input/label "Password"
                    :input/placeholder "Enter your password"
                    :input/required true
                    :input/value ""
                    :input/type "password"}
                   {:form/name "loginform"
                    :form/inputs [[:input/name "loginform/name"]
                                  [:input/name "loginform/password"]]}
                   {:error/name "loginpage/error"
                    :error/msg  "" }
                   ])

(d/transact! conn [{:app/title "ndt"
                    :app/page  ""
                    :app/auth false}])
;;helper functions
(defn getentval [ent valkey]
  (valkey ent))


