/* //////////////////////////////////////////////////////// */
/*                          DASHBOARD                       */
/* //////////////////////////////////////////////////////// */

/* --- Emprunts en cours --- */
GO
CREATE OR ALTER VIEW Dashboard_Loan_Ongoing
AS SELECT *
   FROM loan
   WHERE date_returned IS NULL ;

/* --- Retards --- */
GO
CREATE OR ALTER VIEW Dashboard_Loan_Retards
AS SELECT *
   FROM loan
   WHERE DATEADD(DAY, 14, date_loaned) < GETDATE() AND date_returned IS NULL; ;

/* --- Réservations en cours --- */
GO
CREATE OR ALTER VIEW Dashboard_WaitingList_Pending
AS SELECT * FROM waiting_list WHERE status_reservation='PENDING';

/* --- Livres lus--- */
-- GO
-- CREATE OR ALTER VIEW Dashboard_Book_Read_ByUser
-- AS
-- SELECT b.id, b.author, b.category, b.cover_url, b.description, b.isbn, b.title, l.date_loaned, l.user_id
-- FROM book b
--          INNER JOIN book_copy c ON b.id = c.book_id
--          INNER JOIN loan l ON l.copy_id = c.id
-- WHERE l.date_loaned IS NOT NULL ;

GO
CREATE OR ALTER VIEW Dashboard_BookLoan_Read_ByUser
AS
SELECT l.*
FROM book b
INNER JOIN book_copy c ON b.id = c.book_id
INNER JOIN loan l ON l.copy_id = c.id
WHERE l.date_loaned IS NOT NULL ;

/* --- Total livres --- */
GO
CREATE OR ALTER VIEW Dashboard_NbTotal_Book
AS
SELECT COUNT(*) AS nbBook
FROM book AS b
         LEFT JOIN book_copy as c ON b.id=c.book_id
WHERE NOT(state = 'RETIRED');

/* --- Top 10 Livres les + empruntés */
GO
CREATE OR ALTER VIEW Dashboard_Most_Read_Books
AS
SELECT DISTINCT b.id, b.author, b.category, b.cover_url, b.description, b.isbn, b.title, COUNT(*) AS nbLoans
FROM book b
         INNER JOIN book_copy c ON b.id = c.book_id
         INNER JOIN loan l ON l.copy_id = c.id
WHERE l.date_loaned IS NOT NULL
GROUP BY b.id, b.author, b.category, b.cover_url, b.description, b.isbn, b.title ;


/* supprimer toutes les vues */
/*
DECLARE @sql NVARCHAR(MAX) = '';

SELECT @sql += 'DROP VIEW IF EXISTS [' + SCHEMA_NAME(schema_id) + '].[' + name + '];' + CHAR(13)
FROM sys.views;

EXEC sp_executesql @sql;
*/