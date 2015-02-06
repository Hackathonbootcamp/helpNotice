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
