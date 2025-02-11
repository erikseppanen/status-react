(ns status-im.utils.wallet-connect-legacy
  (:require ["@walletconnect/client-legacy" :default WalletConnect]
            [status-im.utils.config :as config]))

(defn create-connector [uri]
  (WalletConnect.
   (clj->js {:uri uri
             :clientMeta config/default-wallet-connect-metadata})))