create table groups (
    group_id               VARCHAR(36)     NOT NULL,
    group_name             VARCHAR(1024)   NOT NULL,

    CONSTRAINT groups_pk PRIMARY KEY(group_id)
);