(in-ns 'help-notice.handler)

; 経度・緯度からgogleのurlを取得
(defn get-gmurl [latitude longitude]
  (str "http://maps.google.com/maps?q=" latitude "," longitude))

; 緊急度メッセージを作成
(defn make-severity-msg [severity]
  (if (= 1 severity) "今助けてほしい！" (if (= 2 severity) "今日助けてほしい！" "今週助けてほしい！")))

; 助けますURLを作成
(defn make-helped-url [help_id helper_id]
  (str "http://helpnotice.herokuapp.com/helped.html?help_id=" help_id "&helper_id=" helper_id))

; twitter用メッセージを作成
(defn make-twitter-msg [severity latitude longitude help_id helper_id]
  (str
   (make-severity-msg severity) "\n"
   (get-gmurl latitude longitude) "\n"
   (make-helped-url help_id helper_id)))

(def my-creds (make-oauth-creds "2sEcph9BOK5Hk412wQ8qJaSI1"
                                "IKHsWgt959b693MTCYc5BYMQOXsDqR4I3m9VXqW0zk7sKvyXdA"
                                "2931060518-ICKKL7AoYhMoCjuztp5f4UVpWmGNMiAItzAwWm1"
                                "CXJECGtp9SW7wA3smz4Gm5ewdm0PQ1VA2gCouBcHC3XDc"))

; clojureybotから指定ユーザに対してメッセージを送信する
(defn send-twitter-msg [toname text]
  (:code (:status (direct-messages-new :oauth-creds my-creds :params {:screen_name toname :text text}))))
