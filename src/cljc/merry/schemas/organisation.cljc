(ns merry.schemas.organisation)


(def organisation-model
  {:organisation
   {:schema nil
    :name [string? {:error/message "Name cannot be blank"}]
    :address-line-1 string?
    :address-line-2 any?
    :city string?
    :county string?
    :postcode string?
    :country string?
    :telephone string?
    :email string?}})
