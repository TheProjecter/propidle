create table sessions (
    session_id             varchar(36)     NOT NULL,
    session_username       varchar(256)    NOT NULL,
    session_start_time     timestamp       NOT NULL,

    CONSTRAINT sessions_pk PRIMARY KEY(session_id)
);