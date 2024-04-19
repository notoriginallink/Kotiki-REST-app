INSERT INTO kotiki.owners (owner_id, first_name, last_name, birthdate)
VALUES
    (DEFAULT, 'Lenochka', 'Zayka', '2004-06-06'),
    (DEFAULT, 'Danya', 'Solnyshko', '2004-02-25'),
    (DEFAULT, 'Danya', 'Starikowsky', '2003-04-18');

INSERT INTO kotiki.cats (cat_id, name, breed, color, birthdate, owner_id)
VALUES
    (DEFAULT, 'Gavrusha', 'Abyssinian', 'ORANGE', '2020-05-07', 2),
    (DEFAULT, 'Stepa', 'Siamese', 'GREY', '2019-09-10', 2),
    (DEFAULT, 'Strike', 'Cavalier King Charles Spaniel', 'WHITE', '2022-01-19', 1),
    (DEFAULT, 'Matroskin', 'Dvornyaga', 'GREY', '2012-04-03', 2),
    (DEFAULT, 'Markus', 'Fatty', 'BLACK', '2015-07-23', 3);
