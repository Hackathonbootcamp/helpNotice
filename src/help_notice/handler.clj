(ns help-notice.handler
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback))
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:require [clojure.java.jdbc :as j])
  (:use [cheshire.core])
  (:require [clj-time.local :as l])
  (:require [clj-time.coerce :as c]))

;マッチングする際の半径（m）
(def MATCH_RADIUS 1000)

(load "http")
(load "db")
(load "twitter")

;2-3-4
(defn helpme [need_help_id severity latitude longitude]
  (do
    (let [help_id (apply :help_id (regist-help need_help_id severity latitude longitude))]
      (doseq [i (match-helper latitude longitude MATCH_RADIUS)]
        (send-twitter-msg (:social_id i) (make-twitter-msg severity latitude longitude help_id (:helper_id i)))))
    (res-json (str "{\"success\": " true "}"))
    ))

;3-4
(defn match [help_id]
  (do
    (let [help (get-help help_id)]
      (doseq [i (match-helper (apply :help_latitude help) (apply :help_longitude help) MATCH_RADIUS)]
        (send-twitter-msg (:social_id i) (make-twitter-msg (apply :severity help) (apply :help_latitude help) (apply :help_longitude help) help_id (:helper_id i)))))
    (res-json (str "{\"success\": " true "}"))
    ))

;4
(defn notice [help_id helper_id]
  (do
    (let [help (get-help help_id) helper (get-helper helper_id)]
      (send-twitter-msg (apply :social_id helper) (make-twitter-msg (apply :severity help) (apply :help_latitude help) (apply :help_longitude help) help_id helper_id)))
    (res-json (str "{\"success\": " true "}"))
    ))

;5
(defn helped [help_id helper_id]
  (if (not-helped? help_id)
    (do
      (regist-helper help_id helper_id)
      (res-json (str "{\"success\": " true "}")))
    (res-json (str "{\"success\": " false "}"))))

(defroutes app-routes
  (GET "/" [] "running")
  (GET "/helpme" {params :params}
       (helpme (Long/valueOf (params :need_help_id)) (Integer/valueOf (params :severity)) (bigdec (params :latitude)) (bigdec(params :longitude))))
  (GET "/match" {params :params}
       (match (Long/valueOf (params :help_id))))
  (GET "/notice" {params :params}
       (notice (Long/valueOf (params :help_id)) (Long/valueOf (params :helper_id))))
  (GET "/helped" {params :params}
       (helped (Long/valueOf (params :help_id)) (Long/valueOf (params :helper_id))))
  (GET "/helpinfo" {params :params}
       (res-json (generate-string (first (get-help-info (Long/valueOf (params :help_id)) (Long/valueOf (params :helper_id)))))))
  (route/files "/")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
