create table changes (
    properties_path        VARCHAR(256)     NOT NULL,
    revision_number        INTEGER          NOT NULL,
    property_name          VARCHAR(256)     NOT NULL,
    previous_value         VARCHAR(1024),
    updated_value          VARCHAR(1024),

    CONSTRAINT changes_pk PRIMARY KEY(properties_path, revision_number, property_name)
);