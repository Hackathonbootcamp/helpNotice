(in-ns 'help-notice.handler)

(def postgresql-db {:subprotocol "postgresql"
                    :subname "//ec2-107-21-118-56.compute-1.amazonaws.com:5432/db5n0b7n4g0p8a?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"
                    :user "hyydrykskvwrkw"
                    :password "VLf448tQQ1hhW4XGB_lM4TjFRK"})

(defn now []
  (c/to-sql-time (l/local-now)))

(defn regist-help [need_help_id severity latitude longitude]
  (j/insert!
   postgresql-db
   :help
   {:need_help_id need_help_id :severity severity :help_latitude latitude :help_longitude longitude :help_datetime (now)}))

(defn not-helped? [help_id]
  (nil? (apply :helper_id (j/query postgresql-db
           ["select helper_id from help where help_id = ?" help_id]))))

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
           ["select help_id, need_help_id, severity, help_latitude, help_longitude from help where help_id = ?" help_id]))

(defn get-helper [helper_id]
  (j/query postgresql-db
           ["select helper_id, helper_name, social_id from helper where helper_id = ?" helper_id]))

(defn get-need-helper [need_help_id]
  (j/query postgresql-db
           ["select need_help_id, need_help_name, need_help_address, need_help_tel from need_helper where need_help_id = ?" need_help_id]))

(defn get-help-info [help_id helper_id]
  (j/query postgresql-db
           ["
            select
                req.help_id,
                req.helper_id,
                helper.helper_name,
                help.need_help_id,
                need_helper.need_help_name,
                need_helper.need_help_address,
                need_helper.need_help_tel,
                case help.severity
                    when 1 then '今助けて！'
                    when 2 then '今日助けて！'
                    else '今週助けて！' end as severity,
                to_char(help.help_latitude, '99D999999') as latitude,
                to_char(help.help_longitude, '999D999999') as longitude,
                to_char(help.help_datetime, 'YYYY/MM/DD HH24:MI:SS') as datetime,
                case when help.helped_datetime is null then false else true end as helped
            from
                (select ? as help_id, ? as helper_id) req
                left outer join helper
                    on helper.helper_id = req.helper_id
                left outer join help
                    on help.help_id = req.help_id
                left outer join need_helper
                    on need_helper.need_help_id = help.need_help_id
            " help_id helper_id]))
