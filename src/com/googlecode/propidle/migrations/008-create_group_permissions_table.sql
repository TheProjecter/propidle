create table group_permissions (
    group_id               VARCHAR(36)     NOT NULL,
    permission             VARCHAR(256)    NOT NULL,

    CONSTRAINT group_permissions_pk PRIMARY KEY(group_id, permission)
);