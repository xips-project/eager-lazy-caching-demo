INSERT INTO author (name, lastname, birth, death)
VALUES
    ('Herman', 'Melville', '1819-08-01', '1891-09-28'),
    ('Emily', 'Brontë', '1818-07-30', '1848-12-19'),
    ('Fyodor', 'Dostoevsky', '1821-11-11', '1881-02-09'),
    ('Virginia', 'Woolf', '1882-01-25', '1941-03-28'),
    ('Oscar', 'Wilde', '1854-10-16', '1900-11-30'),
    ('Jane', 'Austen', '1775-12-16', '1817-07-18'),
    ('William', 'Shakespeare', '1564-04-26', '1616-04-23'),
    ('J.R.R.', 'Tolkien', '1892-01-03', '1973-09-02'),
    ('Leo', 'Tolstoy', '1828-09-09', '1910-11-20'),
    ('Agatha', 'Christie', '1890-09-15', '1976-01-12');


INSERT INTO book (title, publication_year)
VALUES
    ('Moby-Dick', 1851),
    ('Wuthering Heights', 1847),
    ('Crime and Punishment', 1866),
    ('To the Lighthouse', 1927),
    ('The Picture of Dorian Gray', 1890),
    ('Pride and Prejudice', 1813),
    ('Hamlet', 1603),
    ('The Hobbit', 1937),
    ('War and Peace', 1869),
    ('Murder on the Orient Express', 1934);

-- Herman Melville
INSERT INTO author_books (author_id, book_id)
VALUES
    (1, 1),
    (1, 7);

-- Emily Brontë
INSERT INTO author_books (author_id, book_id)
VALUES
    (2, 2);

-- Fyodor Dostoevsky
INSERT INTO author_books (author_id, book_id)
VALUES
    (3, 3);

-- Virginia Woolf
INSERT INTO author_books (author_id, book_id)
VALUES
    (4, 4);

-- Oscar Wilde
INSERT INTO author_books (author_id, book_id)
VALUES
    (5, 5);

-- Jane Austen
INSERT INTO author_books (author_id, book_id)
VALUES
    (6, 6);

-- William Shakespeare
INSERT INTO author_books (author_id, book_id)
VALUES
    (7, 7),
    (7, 8),
    (7, 10);

-- J.R.R. Tolkien
INSERT INTO author_books (author_id, book_id)
VALUES
    (8, 9);

-- Leo Tolstoy
INSERT INTO author_books (author_id, book_id)
VALUES
    (9, 9);

-- Agatha Christie
INSERT INTO author_books (author_id, book_id)
VALUES
    (10, 10);
