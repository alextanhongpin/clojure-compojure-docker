(ns hello-world.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response]]
            [hello-world.math :as math]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

; curl localhost:3000
(defn get-handler [request]
  (println (math/sum 1 2))
  (response {:message "hello world"}))

; curl -XPOST -H "Content-Type: application/json" -d '{"name": "john doe"}' localhost:3000
(defn post-handler [request]
  (let [name (get-in request [:body "name"])]
    (response {:name name})))

(defroutes app-routes
  (GET "/" [] get-handler) 
  (POST "/" [] post-handler)
  (route/not-found "Not Found"))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body)
      (wrap-json-response)))
