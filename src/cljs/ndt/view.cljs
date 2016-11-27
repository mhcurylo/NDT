(ns ndt.ui
  (:require 
    [cljs.core.async :as async]
    [rum.core :as rum]
    [datascript.core :as d]
    [ndt.ds :as ds]))

(defn setnbsp []
     {:dangerouslySetInnerHTML {:__html "&nbsp;"}})

(rum/defc textinput [db bus inputent] 
  (let [[inputname label placeholder inputvalue inputtype] 
        (map (partial ds/getentval inputent) 
             [:input/name
              :input/label
              :input/placeholder
              :input/value
              :input/type])]
  [:div [:label {:for label} label] 
        [:input.u-full-width  {:type inputtype 
                               :value inputvalue
                               :on-input (fn [e] (async/put! bus [:set-input-value
                                                                   db
                                                                   inputname 
                                                                   (.-value (.-target e))]))
                               :placeholder placeholder}]])) 

(rum/defc renderforminputs [db bus forminputs] 
  [:form (map (fn [x] (rum/with-key (textinput db bus x) (:input/name x))) forminputs)])

(rum/defc ndtlogo []
    [:#ndtlogo [:h1 "NDT"]])

(rum/defc loginform [db bus]
  [:#loginform [:.blank] 
               (renderforminputs db bus (:form/inputs (d/entity @db [:form/name "loginform"])))
               [:button.pull-right "SUBMIT"]])


(rum/defc loginpage [db bus]
     [:.row [:.three.columns (setnbsp)]
            [:.six.columns (ndtlogo) (loginform db bus)]])

(defn mountndt [page db bus]
  (rum/mount (page db bus)
           (. js/document (getElementById "app"))))

