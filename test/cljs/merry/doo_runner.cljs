(ns merry.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [merry.core-test]))

(doo-tests 'merry.core-test)

