(ns merry.views)

(def parent-form
  {:path :parent
   :type :form
   :actions [{
              :type :submit-button
              :dispatch :submit-form
              :route-name :merry.events/ping
              :parameters {:user-id [:data :parent :first-name]}
              :on-success [:update-parents]}

             {:label "Cancel"
              :type :button
              :dispatch [:cancel-form :parent]
              :style "btn-secondary"}]  

   :fields [{:path :first-name
             :label "First Name"
             :type :text-input}

            {:path :last-name
             :label "Last Name"
             :type :text-input}

            {:path :small-number
             :label "Small Number"
             :type :text-input}

            {:path :large-number
             :label "Large Number"
             :type :text-input}

            {:path :numbers
             :type :collection
             :fields [
                      {:path :no
                       :label "No"
                       :type :text-input}

                      {:path :wibble
                       :label "Wobble Foo"
                       :type :text-input}
                      ]}]})


(def list-parents
  {:path :parents
   :type :list
   :actions [{:label "Details"
              :type :button
              :dispatch :parent-details
              :parameters [:id]
              :style "btn-primary"}]
   })
