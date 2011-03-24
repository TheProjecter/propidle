create table aliases (
    from_resource          VARCHAR(1024)     NOT NULL,
    to_url                 VARCHAR(1024)     NOT NULL,

    CONSTRAINT aliases_pk PRIMARY KEY (from_resource)
)