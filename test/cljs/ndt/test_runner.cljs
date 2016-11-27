(ns ndt.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [ndt.core-test]
   [ndt.common-test]))

(enable-console-print!)

(doo-tests 'ndt.core-test
           'ndt.common-test)
