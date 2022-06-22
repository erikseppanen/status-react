(ns status-im.switcher.animation
  (:require [quo2.reanimated :as reanimated]
            [status-im.switcher.constants :as constants]))

;; Component Animations
(defn switcher-touchable-on-press-in
  [touchable-scale]
  (reanimated/animate-shared-value-with-timing touchable-scale 0.9 300 :easing1))

(defn switcher-touchable-on-press-out
  [switcher-opened? close-button-opacity switcher-button-opacity button-touchable-scale]
  (reanimated/animate-shared-value-with-timing button-touchable-scale 1 300 :easing2)
  (if @switcher-opened?
    (do
      (reanimated/animate-shared-value-with-timing close-button-opacity 0 300 :easing2)
      (reanimated/animate-shared-value-with-timing switcher-button-opacity 1 300 :easing1))
    (do
      (reanimated/animate-shared-value-with-timing close-button-opacity 1 300 :easing1)
      (reanimated/animate-shared-value-with-timing switcher-button-opacity 0 300 :easing2)))
  (swap! switcher-opened? not))

;; Layout Animations
(defn switcher-layout-animations [view-id]
  (let [{:keys [width height]}      (constants/dimensions)
        half-width                  (/ width 2)
        switcher-bottom-position    (constants/switcher-bottom-position view-id)
        switcher-target-radius      (Math/hypot
                                     half-width
                                     (- height constants/switcher-button-radius switcher-bottom-position))
        switcher-size               (* 2 switcher-target-radius)
        switcher-from-x             (- half-width constants/switcher-button-radius)
        switcher-from-y             (- height switcher-bottom-position constants/switcher-button-size constants/switcher-height-offset)
        switcher-to-x               (- half-width switcher-target-radius)
        switcher-to-y               (- height constants/switcher-button-size switcher-bottom-position switcher-target-radius)
        switcher-entering-animation (.duration (new reanimated/key-frame
                                                    (clj->js
                                                     {:from {:width        constants/switcher-button-size
                                                             :height       constants/switcher-button-size
                                                             :originX      switcher-from-x
                                                             :originY      switcher-from-y
                                                             :borderRadius constants/switcher-button-radius}
                                                      :to   {:width        switcher-size
                                                             :height       switcher-size
                                                             :originX      switcher-to-x
                                                             :originY      switcher-to-y
                                                             :borderRadius switcher-target-radius}})) 300)
        switcher-exiting-animation  (.duration (new reanimated/key-frame
                                                    (clj->js {:from {:width        switcher-size
                                                                     :height       switcher-size
                                                                     :originX      switcher-to-x
                                                                     :originY      switcher-to-y
                                                                     :borderRadius switcher-target-radius}
                                                              :to   {:width        constants/switcher-button-size
                                                                     :height       constants/switcher-button-size
                                                                     :originX      switcher-from-x
                                                                     :originY      switcher-from-y
                                                                     :borderRadius constants/switcher-button-radius}})) 300)
        container-from-x             (- switcher-from-x)
        container-from-y             (- switcher-from-y)
        container-to-x               (- switcher-to-x)
        container-to-y               (- switcher-to-y)
        container-entering-animation (.duration (new reanimated/key-frame
                                                     (clj->js {:from {:originX container-from-x
                                                                      :originY container-from-y
                                                                      :transform [{:scale 0.9}]}
                                                               :to   {:originX container-to-x
                                                                      :originY container-to-y
                                                                      :transform [{:scale 1}]}})) 300)
        container-exiting-animation (.duration (new reanimated/key-frame
                                                    (clj->js {:from {:originX container-to-x
                                                                     :originY container-to-y
                                                                     :transform [{:scale 1}]}
                                                              :to   {:originX container-from-x
                                                                     :originY container-from-y
                                                                     :transform [{:scale 0.9}]}})) 300)]
    {:switcher-entering-animation  switcher-entering-animation
     :switcher-exiting-animation   switcher-exiting-animation
     :container-entering-animation container-entering-animation
     :container-exiting-animation  container-exiting-animation}))
