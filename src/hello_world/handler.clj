(ns hello-world.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [hello-world.math :as math]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clj-time.core :as time]
            [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [buddy.sign.jwt :as jwt]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.token :refer [jws-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]])
  (:import (com.mchange.v2.c3p0 ComboPooledDataSource PooledDataSource)))

(def secret "mysupersecret")

(def auth-backend (jws-backend {:secret secret 
                                :options {:alg :hs512}
                                :token-name "Bearer"}))

(defn home-handler
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (response {:status 200 :body {:message "hello world"}})))

(defn login-handler
  [request]
  (let [claims {:email (get-in request [:body :email])
                :exp (time/plus (time/now) (time/seconds 3600))}
        token (jwt/sign claims secret {:alg :hs512})]
    (response {:token token})))

; curl localhost:3000
(defn get-handler [request]
  (response {:message "hello world" :sum (math/sum 1 2)}))

; curl -XPOST -H "Content-Type: application/json" -d '{"name": "john doe"}' localhost:3000
(defn post-handler [request]
  (let [name (get-in request [:body "name"])]
    (response {:name name})))


(def db-spec {:dbtype "postgres" 
         :dbname (or (System/getenv "DB_NAME") "test") 
         :user (or (System/getenv "DB_USER") "john")
         :password (or (System/getenv "DB_PASS") "123456")
         :host (or (System/getenv "DB_HOST") "127.0.0.1")
         :port (or (System/getenv "DB_PORT") 5432) })

; (def ds (jdbc/get-datasource db-spec))
; (println "query 1 + 1" (:count (jdbc/execute-one! ds ["select 1 + 1 as count"])))
(with-open [^PooledDataSource ds (connection/->pool ComboPooledDataSource db-spec)]
  (println "query 1 + 1:" (:count (jdbc/execute-one! ds ["select 1 + 1 as count"]))))

(defroutes app-routes
  (GET "/home" [] home-handler)
  (POST "/login" [] login-handler)
  (GET "/" [] get-handler) 
  (POST "/" [] post-handler)
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-authentication auth-backend)
      (wrap-authorization auth-backend)
      (wrap-json-body)
      (wrap-json-response)))
