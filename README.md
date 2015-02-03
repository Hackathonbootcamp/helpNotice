# help_notice

確認くんの通知周り担当

テーブル情報
<pre>
create table help (
help_id serial primary key,
need_help_id integer,
helper_id integer,
severity integer,
help_latitude numeric(8,6),
help_longitude numeric(9,6),
help_datetime timestamp,
helped_datetime timestamp);
</pre>
<pre>
create table base (
base_id integer primary key,
base_name varchar(256),
base_latitude numeric(8,6),
base_longitude numeric(9,6));
</pre>
<pre>
create table helper(
helper_id integer primary key,
helper_name varchar(256),
social_id varchar(256));
</pre>
<pre>
create table helper_base(
helper_id integer,
base_id integer,
primary key (helper_id, base_id));
</pre>

## /helpme?need_help_id&severity&latitude&longitude

・helpデータ登録
・helperマッチング
・マッチング結果に対するtwitter通知

## /match?help_id

・help_idによる登録済みデータを元にhelperマッチング
・マッチング結果に対するtwitter通知

## /notice?help_id&params :helper_id

・help_idによる登録済みデータを元に、helper_idに対してtwitter通知

## /helped?help_id&helper_id

・help_idによる登録済みデータに対して、helper情報を更新

## License

Copyright © 2015 hisataka
