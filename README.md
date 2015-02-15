# help_notice

## デプロイ先

http://helpnotice.herokuapp.com/

## 画面

### /helped.html?help_id&helper_id&key

* 助けます登録画面

## API

### /helpme?need_help_id&severity&latitude&longitude

* helpデータ登録
* helperマッチング
* マッチング結果に対するtwitter通知

応答
<pre>{"success": true}</pre>

### /match?help_id

* help_idによる登録済みデータを元にhelperマッチング
* マッチング結果に対するtwitter通知

応答
<pre>{"success": true}</pre>

### /notice?help_id&helper_id

* help_idによる登録済みデータを元に、helper_idに対してtwitter通知

応答
<pre>{"success": true}</pre>

### /helped?help_id&helper_id

* help_idによる登録済みデータに対して、helper情報を更新

応答
<pre>{"success": true}</pre>

### /helpinf?help_id&helper_id&key

* ヘルプ案件情報を取得

### /friend

* clojureybotをフォローしているユーザに対してフォロー返しを行う

応答
<pre>{"success": true}</pre>

応答
<pre>
{"help_id": long,
 "helper_name": string,
 "need_help_id": long,
 "need_help_name": string,
 "need_help_address": string,
 "need_help_tel": string,
 "severity": string,
 "latitude": string,
 "longitude": string,
 "datetime": string,
 "helped": boolean,
 "key": text
}
</pre>

## License

Copyright © 2015 hisataka
