(ns merry.events
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [malli.core :as m]
   [malli.util :as mu]
   [malli.error :as me]
   [malli.transform :as mt]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]
   [merry.model :as model]))

;;dispatchers

(rf/reg-event-db
 :common/navigate
 (fn [db [_ match]]
   (let [old-match (:common/route db)
         new-match (assoc match :controllers
                          (rfc/apply-controllers (:controllers old-match) match))]
     (assoc db :common/route new-match))))

(rf/reg-fx
 :common/navigate-fx!
 (fn [[k & [params query]]]
   (rfe/push-state k params query)))

(rf/reg-event-fx
 :common/navigate!
 (fn [_ [_ url-key params query]]
   {:common/navigate-fx! [url-key params query]}))

(rf/reg-event-db
 :set-docs
 (fn [db [_ docs]]
   (assoc db :docs docs)))

(rf/reg-event-fx
 :fetch-docs
 (fn [_ _]
   {:http-xhrio {:method          :get
                 :uri             "/docs"
                 :response-format (ajax/raw-response-format)
                 :on-success       [:set-docs]}}))

(rf/reg-event-db
 :common/set-error
 (fn [db [_ error]]
   (assoc db :common/error error)))

(rf/reg-event-fx
 :page/init-home
 (fn [_ _]
   {:dispatch [:fetch-docs]}))

(rf/reg-event-db
 :initialise-db
 (fn [db _]
   (->
   
    (assoc db :data {:parent
                  {:first-name "Blanche"
                   :last-name "Simmons"
                   :small-number 0
                   :large-number 0
                   :numbers [{:no 2 :wibble "www"} {:no 22 :wibble "llk"}]}})

    (assoc :schema model/parent-model))))

(rf/reg-event-db
 :update
 (fn [db [_ data-path schema-path value]]
   (let [schema-path (into [:schema]  schema-path)
         schema (get-in db schema-path )
         converted-value (m/decode schema value mt/string-transformer)
         valid? (m/validate schema converted-value)]
     (if valid?
       (->
        (assoc-in db (into [:data] data-path) converted-value)
        (assoc-in  (into [:error] data-path) nil))
       (->
        (assoc-in db (into [:error2] data-path)
                  (-> (m/explain schema converted-value)
                      (me/humanize))))))))

;; subscriptions


(rf/reg-sub
 :common/route
 (fn [db _]
   (-> db :common/route)))

(rf/reg-sub
 :common/page-id
 :<- [:common/route]
 (fn [route _]
   (-> route :data :name)))

(rf/reg-sub
 :common/page
 :<- [:common/route]
 (fn [route _]
   (-> route :data :view)))

(rf/reg-sub
 :docs
 (fn [db _]
   (:docs db)))

(rf/reg-sub
 :common/error
 (fn [db _]
   (:common/error db)))

(rf/reg-sub
 :document
 (fn [db [_ path]]
   (let [doc (:document db)]
     (get-in doc path))))

(rf/reg-sub
 :data
 (fn [db [_ path]]
   (let [doc (:data db)]
     (get-in doc path))))

(rf/reg-sub
 :error
 (fn [db [_ path]]
   (let [error (:error2 db)]
     (first (get-in error path)))))
(rf/reg-sub
 :item-count
 (fn [db [_ path]]
   (let [doc (:data db)]
     (count (get-in doc path)))))
