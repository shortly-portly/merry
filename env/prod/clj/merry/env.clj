(ns merry.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[merry started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[merry has shut down successfully]=-"))
   :middleware identity})
