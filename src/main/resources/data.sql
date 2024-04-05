-- Inserting publishers
INSERT INTO publisher (name)
VALUES ('Penguin Random House'),
       ('HarperCollins'),
       ('Simon & Schuster'),
       ('Macmillan'),
       ('Hachette Livre'),
       ('Pearson'),
       ('Scholastic'),
       ('Bloomsbury');

-- Inserting authors
INSERT INTO author (birth, lastname, name)
VALUES ('1978-03-21', 'Miller', 'James'),
       ('1985-06-14', 'Robinson', 'Emma'),
       ('1972-11-30', 'Garcia', 'David'),
       ('1989-09-05', 'Murphy', 'Olivia'),
       ('1982-02-18', 'Jones', 'William'),
       ('1976-08-23', 'Davis', 'Sophia'),
       ('1983-05-07', 'Martinez', 'Daniel'),
       ('1991-12-10', 'Brown', 'Ava'),
       ('1970-10-02', 'Wilson', 'Matthew'),
       ('1988-04-15', 'Taylor', 'Chloe'),
       ('1979-07-29', 'Anderson', 'Ethan'),
       ('1986-09-22', 'Thomas', 'Isabella'),
       ('1973-04-14', 'Moore', 'Alexander'),
       ('1990-11-17', 'White', 'Mia');

-- Inserting books
INSERT INTO book (publication_year, publisher_id, title)
VALUES (2012, 1, 'The Silent Patient'),
       (2019, 2, 'Where the Crawdads Sing'),
       (2005, 3, 'The Da Vinci Code'),
       (2017, 1, 'Becoming'),
       (2010, 4, 'The Hunger Games'),
       (2014, 5, 'Gone Girl'),
       (2003, 6, 'The Kite Runner'),
       (2015, 7, 'Harry Potter and the Cursed Child'),
       (2007, 8, 'Harry Potter and the Deathly Hallows'),
       (2009, 1, 'The Help'),
       (2011, 2, 'The Night Circus'),
       (2018, 3, 'Crazy Rich Asians'),
       (2016, 4, 'The Girl on the Train'),
       (2013, 5, 'Divergent'),
       (2006, 6, 'The Secret'),
       (2012, 7, 'The Fault in Our Stars'),
       (2019, 8, 'Where the Forest Meets the Stars');

-- Inserting author_book relationships with correct book_ids
INSERT INTO author_books (author_id, book_id)
VALUES (1, 1),   -- James Miller wrote "The Silent Patient"
       (2, 2),   -- Emma Robinson wrote "Where the Crawdads Sing"
       (3, 3),   -- David Garcia wrote "The Da Vinci Code"
       (1, 4),   -- James Miller co-authored "Becoming"
       (4, 4),   -- Olivia Murphy co-authored "Becoming"
       (5, 5),   -- William Jones wrote "The Hunger Games"
       (6, 6),   -- Sophia Davis wrote "Gone Girl"
       (7, 7),   -- Daniel Martinez wrote "The Kite Runner"
       (8, 8),   -- Ava Brown wrote "Harry Potter and the Cursed Child"
       (9, 9),   -- Matthew Wilson wrote "Harry Potter and the Deathly Hallows"
       (10, 10), -- Chloe Taylor wrote "The Help"
       (11, 11), -- Ethan Anderson wrote "The Night Circus"
       (12, 12), -- Isabella Thomas wrote "Crazy Rich Asians"
       (13, 13), -- Alexander Moore wrote "The Girl on the Train"
       (14, 14), -- Mia White wrote "Divergent"
       (5, 15),  -- William Jones wrote "The Secret"
       (6, 16),  -- Sophia Davis wrote "The Fault in Our Stars"
       (7, 17),  -- Daniel Martinez wrote "Where the Forest Meets the Stars"
       (8, 17); -- Ava Brown co-authored "Where the Forest Meets the Stars"
