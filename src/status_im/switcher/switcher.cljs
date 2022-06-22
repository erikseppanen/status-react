(ns status-im.switcher.switcher
  (:require [reagent.core :as reagent]
            [quo2.reanimated :as reanimated]
            [status-im.switcher.styles :as styles]
            [status-im.switcher.animation :as animation]
            [status-im.ui.components.icons.icons :as icons]
            [status-im.react-native.resources :as resources]
            [status-im.switcher.switcher-container :as switcher-container]))

(defn switcher-button
  [view-id close-button-opacity switcher-button-opacity
   button-touchable-scale toggle-switcher-screen-fn]
  [:>
   (fn []
     (let [touchable-original-style       (styles/switcher-button-touchable view-id)
           close-button-original-style    (styles/switcher-close-button)
           switcher-button-original-style (styles/switcher-button)
           touchable-animated-style       (reanimated/apply-animations-to-style
                                           {:transform [{:scale button-touchable-scale}]}
                                           touchable-original-style)
           close-button-animated-style    (reanimated/apply-animations-to-style
                                           {:opacity close-button-opacity}
                                           close-button-original-style)
           switcher-button-animated-style (reanimated/apply-animations-to-style
                                           {:opacity switcher-button-opacity}
                                           switcher-button-original-style)]
       (reagent/as-element
        [reanimated/touchable-opacity {:active-opacity 1
                                       :on-press-in    #(animation/switcher-touchable-on-press-in
                                                         button-touchable-scale)
                                       :on-press-out   toggle-switcher-screen-fn
                                       :style          touchable-animated-style}
         [reanimated/view {:style close-button-animated-style}
          [icons/icon :main-icons/close {:color :black}]]
         [reanimated/image {:source (resources/get-image :status-logo)
                            :style  switcher-button-animated-style}]])))])

(defn switcher-screen [switcher-opened? view-id toggle-switcher-screen-fn]
  (let [{:keys [switcher-entering-animation switcher-exiting-animation
                container-entering-animation container-exiting-animation]}
        (animation/switcher-layout-animations view-id)]
    (when @switcher-opened?
      [reanimated/view {:entering switcher-entering-animation
                        :exiting  switcher-exiting-animation
                        :style    (styles/switcher-screen)}
       [reanimated/view {:entering container-entering-animation
                         :exiting  container-exiting-animation
                         :style    (styles/switcher-screen-container)}
        [switcher-container/tabs toggle-switcher-screen-fn]]])))

(defn switcher [view-id]
  [:>
   (fn []
     (let [switcher-opened?           (reagent/atom false)
           close-button-opacity       (reanimated/use-shared-value 0)
           switcher-button-opacity    (reanimated/use-shared-value 1)
           button-touchable-scale     (reanimated/use-shared-value 1)
           toggle-switcher-screen-fn  #(animation/switcher-touchable-on-press-out
                                        switcher-opened? close-button-opacity switcher-button-opacity button-touchable-scale)]
       (reagent/as-element
        [:<>
         [switcher-screen switcher-opened? view-id toggle-switcher-screen-fn]
         [switcher-button view-id close-button-opacity switcher-button-opacity
          button-touchable-scale toggle-switcher-screen-fn]])))])
