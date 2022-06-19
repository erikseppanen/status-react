(ns status-im.ui.components.plus-button
  (:require [quo.design-system.colors :as colors]
            [quo.core :as quo]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.icons.icons :as icons]))

(def action-button-container
  {:align-items     :center
   :justify-content :center
   :height          32})

(defn action-button []
  {:width            32
   :height           32
   :background-color (:interactive-01 @colors/theme)
   :border-radius    18
   :align-items      :center
   :justify-content  :center})

(defn plus-button [{:keys [on-press loading accessibility-label]}]
  [react/view action-button-container
   [quo/button {:type                :scale
                :accessibility-label (or accessibility-label :plus-button)
                :on-press            on-press}
    [react/view (action-button)
     (if loading
       [react/activity-indicator {:color     colors/white-persist
                                  :animating true}]
       [icons/icon :main-icons/add {:color colors/white-persist}])]]])