(ns merry.widget
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))

(defmulti widget :type)


(defmethod widget :text-input [{:keys [data-path schema-path label]}]
  (let [value (r/atom @(rf/subscribe [:data data-path]))]
    (fn []
      (let [error @(rf/subscribe [:error data-path])]
        [:div.form-group
         [:label label]
         [:input.form-control
         {:type :text
          :class (if error "is-invalid" "is-valid")
          :value @value
          :on-change #(reset! value (-> % .-target .-value))
          :on-blur #(rf/dispatch [:update data-path schema-path @value])}]
        (if error [:div.invalid-feedback error]) ]))))

(defmethod widget :button [{:keys [label dispatch style]}]
  (fn []
    [:button.btn.mr-2 {:class style
                  :on-click #(rf/dispatch dispatch)} label]))

(defmethod widget :form [form-data]
  (fn []
    (let [root   [(:path form-data)]
          fields (:fields form-data)
          actions (:actions form-data)]
      [:div
       (for [field fields]
         (let [data-path   (conj root (:path field))
               schema-path (conj root (:path field))
               widget-data (assoc field :data-path data-path :schema-path schema-path)]
           ^{:key (:path field)} [widget widget-data]))

        (for [action actions]
          ^{:key (:dispatch action)} [widget action])])))

(defmethod widget :collection [collection-data]
  (fn []
    (let [root (:data-path collection-data)
          fields (:fields collection-data)
          item-count @(rf/subscribe [:item-count root])]
      [:div
       (for [index (range item-count)]
         (for [field fields]
           (let [data-path (conj root index (:path field))
                 schema-path (conj root (:path field))
                 widget-data (assoc field :data-path data-path :schema-path schema-path)]
         ^{:key (:path field)} [widget widget-data])))])))

