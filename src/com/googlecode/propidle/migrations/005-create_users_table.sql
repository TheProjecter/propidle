create table users (
    username               VARCHAR(36)    NOT NULL,
    password_hash          NUMERIC         NOT NULL,

    CONSTRAINT users_pk PRIMARY KEY(username)
);