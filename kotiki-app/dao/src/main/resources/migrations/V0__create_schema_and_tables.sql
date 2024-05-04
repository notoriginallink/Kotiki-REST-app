CREATE SCHEMA IF NOT EXISTS kotiki;

CREATE TABLE IF NOT EXISTS kotiki.owners (
    id    bigint GENERATED BY DEFAULT AS IDENTITY,
    first_name  varchar(255) NOT NULL,
    last_name   varchar(255) NOT NULL,
    birthdate   date,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS kotiki.cats (
    id      bigint GENERATED BY DEFAULT AS IDENTITY,
    name        varchar(255) NOT NULL,
    breed       varchar(255),
    color       varchar(255),
    birthdate   date,
    owner_id    bigint REFERENCES kotiki.owners(id),
    PRIMARY KEY (id),
    CONSTRAINT cats_color_check
        CHECK (color::text = ANY
               (ARRAY
                   [
                   'BLACK'::character varying,
                   'WHITE'::character varying,
                   'GREY'::character varying,
                   'ORANGE'::character varying,
                   'SEMICOLOR'::character varying,
                   'BLACK_WHITE'::character varying
                   ]::text[])
            )
);

CREATE TABLE IF NOT EXISTS kotiki.friends (
    first_cat_id    bigint NOT NULL REFERENCES kotiki.cats(id),
    second_cat_id   bigint NOT NULL REFERENCES kotiki.cats(id),
    PRIMARY KEY (first_cat_id, second_cat_id)
);