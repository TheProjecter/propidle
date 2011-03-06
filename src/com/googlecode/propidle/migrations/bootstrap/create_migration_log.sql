create table migration_log (
    migration_number    integer       not null,
    migration_date      timestamp     not null,
    migration_name      varchar(256),

    constraint migration_log_pk primary key(migration_number)
)