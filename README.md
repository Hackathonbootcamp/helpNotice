# help_notice

## デプロイ先

http://helpnotice.herokuapp.com/

## 暫定的に作成したテーブルレイアウト
<pre>
create table help (
help_id bigserial primary key,
need_help_id bigint,
helper_id bigint,
severity integer,
help_latitude numeric(8,6),
help_longitude numeric(9,6),
help_datetime timestamp,
helped_datetime timestamp);
</pre>
<pre>
create table base (
base_id bigint primary key,
base_name text,
base_latitude numeric(8,6),
base_longitude numeric(9,6));
</pre>
<pre>
create table helper(
helper_id bigint primary key,
helper_name text,
social_id text);
</pre>
<pre>
create table helper_base(
helper_id bigint,
base_id bigint,
primary key (helper_id, base_id));
</pre>

## /helpme?need_help_id&severity&latitude&longitude

* helpデータ登録
* helperマッチング
* マッチング結果に対するtwitter通知

## /match?help_id

* help_idによる登録済みデータを元にhelperマッチング
* マッチング結果に対するtwitter通知

## /notice?help_id&helper_id

* help_idによる登録済みデータを元に、helper_idに対してtwitter通知

## /helped?help_id&helper_id

* help_idによる登録済みデータに対して、helper情報を更新

## License

Copyright © 2015 hisataka
