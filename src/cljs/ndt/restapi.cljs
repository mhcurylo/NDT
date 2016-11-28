(ns ndt.restapi
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :as async]))

(defn login [username password]
  (go (let [res (async/<! (http/post "/api/login" {:json-params {:username username :password password}}))]
      (:auth (:body res)))))
