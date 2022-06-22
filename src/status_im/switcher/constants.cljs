(ns status-im.switcher.constants
  (:require [status-im.utils.handlers :refer [<sub]]
            [status-im.utils.platform :as platform]))

;; For translucent status bar, dimensions/window also includes status bar's height,
;; this offset is used for correctly calculating switcher position
(def switcher-height-offset 24)

(def switcher-button-radius 24)

(def switcher-button-size
  (* switcher-button-radius 2))

(def switcher-bottom-positions
  {:android
   {:home-stack 17
    :chat       57}
   :ios
   {:home-stack 35
    :chat       67}})

(defn switcher-bottom-position [view-id]
  (get-in
   switcher-bottom-positions
   [(keyword platform/os) view-id]))

;; TODO(parvesh) - use different height for android and ios(Confirm from Design)
(defn bottom-tabs-height []
  (if platform/android? 55 80))

;; TODO - move to switcher/utils.cljs
(defn dimensions []
  (<sub [:dimensions/window]))
