(in-ns 'help-notice.handler)

; jsonレスポンスを返す
(defn res-json [body]
  {:status 200 :headers {"Content-Type" "application/json; charset=UTF-8"} :body body})
; httpレスポンスを返す
(defn res-http [body]
  {:status 200 :headers {"Content-Type" "text/html; charset=UTF-8"} :body body})
