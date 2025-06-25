CREATE TABLE test_modal
(
    id         BIGINT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    state      SMALLINT NULL,
    CONSTRAINT pk_testmodal PRIMARY KEY (id)
);