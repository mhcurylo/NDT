(ns ndt.ui
  (:require 
    [cljs.core.async :as async]
    [rum.core :as rum]
    [datascript.core :as d]
    [clojure.string :as string]
    [ndt.ds :as ds]))

(defn currentpage [db]
  (:app/page (d/entity @db [:app/title "ndt"])))

(defn setnbsp []
  {:dangerouslySetInnerHTML {:__html "&nbsp;"}})

(defn isformvalid [forminputs]
  (not-any? #(and (:input/required %) (string/blank? (:input/value %))) forminputs))


(rum/defc errorbar [error]
  [:.errorbar.u-full-width [:span.u-full-width.errorbar-msg.red.text-center
                            (:error/msg error)]])

(rum/defc textinput [db bus forminputs] 
  (let [[inputname label placeholder inputvalue inputtype disabled] 
        (map (partial ds/getentval forminputs) 
             [:input/name
              :input/label
              :input/placeholder
              :input/value
              :input/type
              :input/disabled])]
  [:div [:label {:for label} label] 
        [:input.u-full-width  {:type inputtype 
                               :value inputvalue
                               :disabled disabled
                               :on-input #(async/put! bus [:set-input-value db inputname 
                                                                   (.-value (.-target %))])
                               :placeholder placeholder}]])) 

(rum/defc renderforminputs [db bus forminputs] 
  [:form (map #(rum/with-key (textinput db bus %) (:input/name %)) forminputs)])

(rum/defc ndtlogo []
    [:#ndtlogo [:h1 "NDT"]])

(rum/defc loginform [db bus]
  (let [[forminputs error] 
        [(:form/inputs (d/entity @db [:form/name "loginform"]))
         (d/entity @db [:error/name "loginpage/error"])]]
  [:#loginform [:.blank] 
               (renderforminputs db bus forminputs)
               (errorbar error)
               [:button.pull-right {:on-click #(async/put! bus [:submit-login-form db])
                                    :disabled (not (isformvalid forminputs))}
                "SUBMIT"]]))


(rum/defc loginpage [db bus]
     [:.row [:.three.columns (setnbsp)]
            [:.six.columns (ndtlogo) (loginform db bus)]])

(rum/defc homepage [db bus]
     [:.row [:.three.columns]
            [:.six-columns [:h1 "Logged in, you are home."]]])

(rum/defc blankpage [db bus]
     [:div])

(rum/defc routerpage [db bus]
     (let [page (case (currentpage db) 
                  "loginpage" loginpage
                  "homepage" homepage
                  blankpage)]
       (page db bus)))


(defn mountndt [page db bus]
  (rum/mount (page db bus)
           (. js/document (getElementById "app"))))

