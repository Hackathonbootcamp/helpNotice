(in-ns 'help-notice.handler)

; 緊急度メッセージを作成
(defn make-severity-msg [severity]
  (if (= 1 severity) "今助けて！" (if (= 2 severity) "今日助けて！" "今週助けて！")))

; 助けますURLを作成
(defn make-helped-url [help_id helper_id key]
  (str "http://helpnotice.herokuapp.com/helped.html?help_id=" help_id "&helper_id=" helper_id "&key=" key))

; twitter用メッセージを作成
(defn make-twitter-msg [severity latitude longitude help_id helper_id key]
  (str
   (make-severity-msg severity) "\n"
   (make-helped-url help_id helper_id key)))

; 指定ユーザに対してメッセージを送信する
(defn send-twitter-msg [toname text]
  (:code (:status (direct-messages-new
                   :oauth-creds (make-oauth-creds (get-system-val "FROM_CONSUMER_KEY")
                                                  (get-system-val "FROM_CONSUMER_SECRET")
                                                  (get-system-val "FROM_ACCESS_TOKEN")
                                                  (get-system-val "FROM_ACCESS_TOKEN_SECRET"))
                   :params {:screen_name toname :text text}))))

(defn get-followers-ids[]
  (:ids (:body (followers-ids :oauth-creds (make-oauth-creds (get-system-val "FROM_CONSUMER_KEY")
                                                  (get-system-val "FROM_CONSUMER_SECRET")
                                                  (get-system-val "FROM_ACCESS_TOKEN")
                                                  (get-system-val "FROM_ACCESS_TOKEN_SECRET"))))))

(defn get-friends-ids[]
  (:ids (:body (friends-ids :oauth-creds (make-oauth-creds (get-system-val "FROM_CONSUMER_KEY")
                                                  (get-system-val "FROM_CONSUMER_SECRET")
                                                  (get-system-val "FROM_ACCESS_TOKEN")
                                                  (get-system-val "FROM_ACCESS_TOKEN_SECRET"))))))

(defn regist-friend [user_id]
  (friendships-create :oauth-creds (make-oauth-creds (get-system-val "FROM_CONSUMER_KEY")
                                                  (get-system-val "FROM_CONSUMER_SECRET")
                                                  (get-system-val "FROM_ACCESS_TOKEN")
                                                  (get-system-val "FROM_ACCESS_TOKEN_SECRET")) :params {:user_id user_id}))
