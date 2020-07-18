(ns merry.views)

(def parent-form
  {:path :parent
   :type :form
   :actions [{:label "Submit"
              :type :button
              :dispatch [:submit-form :parent]
              :style "btn-primary"}

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
