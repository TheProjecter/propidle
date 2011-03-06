create table group_memberships (
    group_id               VARCHAR(36)     NOT NULL,
    username               VARCHAR(36)     NOT NULL,

    CONSTRAINT group_memberships_pk PRIMARY KEY(group_id, username)
);