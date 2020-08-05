(ns merry.events
  (:require
   [re-frame.core :as rf]
   [ajax.core :as ajax]
   [malli.core :as m]
   [malli.util :as mu]
   [malli.error :as me]
   [malli.transform :as mt]
   [reitit.core :as r]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]
   [merry.schemas.organisation :as org]
   [merry.model :as model]))

;; Routes
(def router
  (r/router
   ["api/"
    ["ping/:user-id" ::ping]]))

(prn (r/routes router))

(defn map-vals
  "Given a map and a function, returns the map resulting from applying
  the function to each value"
  [m f]
  (zipmap (keys m) (map f (vals m))))

(defn create-url
  "Given a route name and a map of parameters return a url.

  The parameter map has keys that correspond to path parameters with the value
  representing a map into the re-frame db"
  [db route-name parameters]
  (let [url-parameters (map-vals parameters (fn [path] (get-in db path)))]
    (prn "routes : " (r/route-names router))
    (prn "route-name :" route-name)
    (prn "url-parameters :" url-parameters)
    (r/match-by-name router route-name url-parameters)))

(rf/reg-event-db
 :set-new-org
 (fn [db [_ result]]
   (prn ":set-new-org called")
   (assoc db :view (:view result) :schema org/organisation-model)))

(rf/reg-event-fx
 :get-new-org
 (fn [{:keys [db]} _]
     {:http-xhrio {:method :get
                   :uri "/api/organisations/new"
                   :format (ajax/transit-request-format)
                   :response-format (ajax/transit-response-format)
                   :on-success [:set-new-org]}}))
(rf/reg-event-fx
 :create-org
 (fn [{:keys [db]} _]
     {:http-xhrio {:method :post
                   :uri "/api/organisations"
                   :format (ajax/transit-request-format)
                   :response-format (ajax/transit-response-format)
                   :on-success [:list-organisations]}}))

;;dispatchers
(rf/reg-event-db
 :submit-form
 (fn [db [_ {:keys [route-name parameters on-success]}]]
   (let [submit-url (create-url db route-name parameters)]
     {:http-xhrio {:method :delete
                   :uri submit-url
                   :format (ajax/transit-request-format)
                   :response-format (ajax/transit-response-format)
                   :on-success on-success}})))

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
        (assoc-in db (into [:error] data-path)
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
   (let [error (:error db)]
     (first (get-in error path)))))
(rf/reg-sub
 :item-count
 (fn [db [_ path]]
   (let [doc (:data db)]
     (count (get-in doc path)))))

(rf/reg-sub
 :new-org
 (fn [db _]
   (:view db)))

(rf/reg-sub
 :new-org2
 (fn [db _]
   (prn ":new org 2 called")
   (if (:view db) (:view db) {:path :first-name :type :text-input})))
