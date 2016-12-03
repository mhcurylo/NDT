(ns ndt.remote.restapi
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [ndt.data.localstorage :as ls]
            [cljs-http.client :as http]
            [cljs.core.async :as async]))

(defn login [username password]
  (go (let [res (async/<! (http/post "/api/login" {:json-params {:username username :password password}}))]
      (if (= (:status res) 200)
        (do (ls/token! (:token (:body res)))
            true)
        (do (ls/token-rm!)
            false)))))
