create table change_details (
    id VARCHAR2(36) NOT NULL,
    revision_number        INTEGER       NOT NULL,
    name          VARCHAR2(1024) NOT NULL,
    value          VARCHAR2(1024) NOT NULL,

    CONSTRAINT change_details_pk PRIMARY KEY (id)
);
GRANT select, insert, update, delete on change_details to PROPIDLE_USER;
