(in-ns 'help-notice.handler)

; postgresql接続用
(def postgresql-db {:subprotocol "postgresql"
                    :subname "//ec2-174-129-1-179.compute-1.amazonaws.com:5432/d76k2v0lvos1l3?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
                    :user "ollzkkdgygzkti"
                    :password "3fdgK_t5FBW2n4-yGs5_D6Xh8f"})

;create table help (
;help_id serial primary key,
;need_help_id integer,
;helper_id integer,
;severity integer,
;help_latitude numeric(8,6),
;help_longitude numeric(9,6),
;help_datetime timestamp,
;helped_datetime timestamp);

;create table base (
;base_id integer primary key,
;base_name varchar(256),
;base_latitude numeric(8,6),
;base_longitude numeric(9,6));

;create table helper(
;helper_id integer primary key,
;helper_name varchar(256),
;social_id varchar(256));

;create table helper_base(
;helper_id integer,
;base_id integer,
;primary key (helper_id, base_id));

(defn now []
  (c/to-sql-time (l/local-now)))

(defn regist-help [need_help_id severity latitude longitude]
  (j/insert!
   postgresql-db
   :help
   {:need_help_id need_help_id :severity severity :help_latitude latitude :help_longitude longitude :help_datetime (now)}))

(defn regist-helper [help_id helper_id]
  (j/update! postgresql-db
    :help
    {:helper_id helper_id :helped_datetime (now)}
   ["help_id=?" help_id]))

(defn match-helper [latitude longitude radius]
  (j/query postgresql-db
           ["
            select
              helper_id,
              helper_name,
              social_id
            from
              helper
            where
              exists (
                select
                  *
                from
                  base
                  left outer join helper_base
                    on base.base_id = helper_base.base_id
                where
                  sqrt(power((base_latitude - ?) * 111000, 2) + power((base_longitude - ?) * 91000, 2)) <= ?
                  and helper_base.helper_id = helper.helper_id
            )
            " latitude longitude radius]))

(defn get-help [help_id]
  (j/query postgresql-db
           ["select help_id, severity, help_latitude, help_longitude from help where help_id = ?" help_id]))

(defn get-helper [helper_id]
  (j/query postgresql-db
           ["select helper_id, helper_name, social_id from helper where helper_id = ?" helper_id]))
