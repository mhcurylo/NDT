(ns ndt.server
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE defroutes context]]
            [compojure.route :refer [resources]]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [environ.core :refer [env]]
            [clj-time.core :as time]
            [buddy.sign.jwt :as jwt]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.token :refer [jws-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defonce secret "secretonagithub")

(defn ok [d] {:status 200 :body d})
(defn bad-request [d] {:status 400 :body d})

;;(if-not (authenticated? request)
;;       (throw-unauthorized)
;;       (ok {loged in: "in pain!"}))


(defn authenticate
  [{username :username password :password}]
      (if (= [username password] ["admin" "password"])
        (let [claims {:user (keyword username)
                      :exp (time/plus (time/now) (time/seconds 3600))}
                token (jwt/sign claims secret {:alg :hs512})]
                      (ok {:token token}))
        (bad-request {:message "wrong auth data"})))

(defroutes routes
  (context "/api" [] 
    (defroutes api-routes
      (POST "/login" {body :body} (authenticate body))))
  (GET "*" _
    {:status 200
     :headers {"Content-Type" "text/html; charset=utf-8"}
     :body (io/input-stream (io/resource "public/index.html"))})
  (resources "/"))

(def auth-backend (jws-backend {:secret secret :options {:alg :hs512}}))

(def http-handler
  (-> routes
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)
      (middleware/wrap-json-body {:keywords? true})
      middleware/wrap-json-response
      (wrap-defaults api-defaults)
      wrap-with-logger
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (run-jetty http-handler {:port port :join? false})))
