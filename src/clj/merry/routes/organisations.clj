(ns merry.routes.organisations
  (:require
   [merry.db.core :as db]
   ))

(def organisations-table
  {:table-name :organisations
   :fields [:id,
            :name,
            :address_line_1,
            :address_line_2,
            :city,
            :county,
            :postcode,
            :country,
            :telephone,
            :email]})

(def organisation-form
  {:path :organisation
   :type :form
   :actions [{
              :type :submit-button
              :dispatch :create-org
              }]
   :fields [{:path :name
             :label "Name"
             :type :text-input}

            {:path :address-line-1
             :label "Address Line 1"
             :type :text-input}

            {:path :address-line-2
             :label "Address Line 2"
             :type :text-input}

            {:path :city
             :label "City"
             :type :text-input}

            {:path :county
             :label "County"
             :type :text-input}

            {:path :postcode
             :label "Postcode"
             :type :text-input}

            {:path :country
             :label "Country"
             :type :text-input}

            {:path :telephone
             :label "Telephone"
             :type :text-input}

            {:path :email
             :label "Email"
             :type :text-input}

            ]})

(def organisation-model
  {:organisation
   {:schema nil
    :name string?
    :address-line-1 string?
    :address-line-2 string?
    :city string?
    :county string?
    :postcode string?
    :country string?
    :telephone string?
    :email string?}})

(def organisation-routes
  ["/organisations"
   [""
    {:get {:summary "returns a list of organisations"
           :handler (fn [_]
                      {:status 200
                       :body (db/get-organisations)})}

     :post {:summary "creates a new organisation, returning the id of the newly created organisation"
            :handler (fn [{:keys [body-params]}]
                       (let [id (-> body-params
                                    (db/create-organisation!)
                                    (first)
                                    ((keyword "last_insert_rowid()")))]
                         {:status 200
                          :body {:id id}}))}}]

   ["/new"
    {:conflicting true
     :get {:summary "Returns a create organisation form"
           :handler (fn [_]
                      {:status 200
                       :body {:view organisation-form}
                       })}}]
   ["/:organisation-id"
    {:conflicting true
     :get {:summary "returns the organisation with the given id"
           :parameters {:path {:organisation-id int?}}
           :handler (fn [{:keys [parameters]}]
                      (let [id (get-in parameters [:path :note-id])]
                        {:status 200
                         :body (db/get-organisation {:id id})}))}

     :put {:summary "updates the organisation with the given id"
           :parameters {:path {:organisation-id int?}}
           :handler (fn [{:keys [body-params]}]
                      (db/update-organisation! body-params)
                      {:status 200
                       :body {:foo 0}})}

     :delete {:summary "deletes the organisation with the given id"
              :parameters {:path {:organisation-id int?}}
              :handler (fn [{:keys [parameters]}]
                         (let [id (get-in parameters [:path :organisation-id])]
                           (db/delete-organisation! {:id id})
                           {:status 200
                            :body {:foo 0}}))}}]])
