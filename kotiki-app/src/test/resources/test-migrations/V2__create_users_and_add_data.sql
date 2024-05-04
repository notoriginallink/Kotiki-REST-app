CREATE TABLE IF NOT EXISTS kotiki.users (
    username        varchar(255)    NOT NULL,
    password        varchar(255),
    role            varchar(255),
    owner_id        bigint          UNIQUE REFERENCES kotiki.owners(id),
    PRIMARY KEY (username)
);

INSERT INTO kotiki.users (username, password, role, owner_id)
VALUES
    ('admin', '$2a$10$YeTWMESION9r/9IQEvHB4OvPcd6M6fzLXaNOMZrIiNB00EBqVXnDm', 'ADMIN', NULL),
    ('LenkaZZZ', '$2a$10$GnFlW5NRRKsZKaV3ZzZi6.rjCq3/30lkJnfwcxf9m2UdOmSPRxY/C', 'USER', 1),
    ('Danya', '$2a$10$bteKvzcQni4Vaa/uQ6q5pul320GrO7LynWvTBg0WDtL0ypdKw3Ete', 'USER', 2),
    ('star', '$2a$10$rcoheU1HMSea94VVYYeSge7FG23qoCogGFGQeQpIqyVnOecqgtHqW', 'USER', 3);
