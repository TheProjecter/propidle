create table change_details (
    id VARCHAR(36) NOT NULL,
    revision_number        INTEGER       NOT NULL,
    name          VARCHAR(1024) NOT NULL,
    value          VARCHAR(1024) NOT NULL,

    CONSTRAINT change_details_pk PRIMARY KEY (id)
);