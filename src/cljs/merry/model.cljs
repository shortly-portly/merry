(ns merry.model)

(def parent-model
  {:parent
   {:schema nil
    :first-name
    [:fn {:error/message "First Name: should be more than 3"} '(fn [value] (< 3 (count value)))]
    :last-name
    [:and
     string?
     [:fn {:error/message "Last Name: should be less than 3"} '(fn [value] (> 3 (count value)))]]
    :small-number
    int?
    :large-number
    int?
    :numbers {:no int?
              :wibble string?}}})
