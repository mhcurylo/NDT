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
}))

;;initiating database with default values / needed data
(d/transact! conn [{:input/name "loginform/name"
                    :input/label "Name"
                    :input/placeholder "Enter your name"
                    :input/value ""
                    :input/type "text"}
                   {:input/name "loginform/password"
                    :input/label "Password"
                    :input/placeholder "Enter your password"
                    :input/value ""
                    :input/type "text"}
                   {:form/name "loginform"
                    :form/inputs [[:input/name "loginform/name"]
                                  [:input/name "loginform/password"]]}
                   ])

(d/transact! conn [{:app/title "ndt"
                    :app/page  "loginpage"
                    :app/auth false}])
;;helper functions
(defn getentval [ent valkey]
  (valkey ent))


