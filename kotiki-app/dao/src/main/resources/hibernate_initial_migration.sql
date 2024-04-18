DROP SCHEMA IF EXISTS kotiki CASCADE;

CREATE TABLE IF NOT EXISTS kotiki.friends
(
    first_cat_id bigint NOT NULL,
    second_cat_id bigint NOT NULL,
    CONSTRAINT friends_pkey PRIMARY KEY (first_cat_id, second_cat_id),
    CONSTRAINT fk20gwkd1v1c23mapenrwy4kj8i FOREIGN KEY (first_cat_id)
    REFERENCES kotiki.cats (cat_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk4jtd192ymwx80rv4uguws8x40 FOREIGN KEY (second_cat_id)
    REFERENCES kotiki.cats (cat_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );

CREATE TABLE IF NOT EXISTS kotiki.owners
(
    owner_id bigint NOT NULL DEFAULT nextval('kotiki.owners_owner_id_seq'::regclass),
    birthdate date,
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT owners_pkey PRIMARY KEY (owner_id)
    );

CREATE TABLE IF NOT EXISTS kotiki.cats
(
    cat_id bigint NOT NULL DEFAULT nextval('kotiki.cats_cat_id_seq'::regclass),
    birthdate date,
    breed character varying(255) COLLATE pg_catalog."default",
    color character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    owner_id bigint,
    CONSTRAINT cats_pkey PRIMARY KEY (cat_id),
    CONSTRAINT fkc0phghv1jwbvelwan7pndwrei FOREIGN KEY (owner_id)
    REFERENCES kotiki.owners (owner_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT cats_color_check CHECK (color::text = ANY (ARRAY['BLACK'::character varying, 'WHITE'::character varying, 'GREY'::character varying, 'ORANGE'::character varying]::text[]))
    );

