create table sessions (
    session_id             VARCHAR(36)     NOT NULL,
    session_username       VARCHAR(256)    NOT NULL,
    session_start_time     TIME            NOT NULL,

    CONSTRAINT sessions_pk PRIMARY KEY(session_id)
);