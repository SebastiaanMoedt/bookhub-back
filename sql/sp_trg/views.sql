
/* //////////////////////////////////////////////////////// */
/*                          DASHBOARD USER                  */
/* //////////////////////////////////////////////////////// */

/* --- Emprunts en cours --- */
GO
CREATE OR ALTER VIEW Dashboard_User_Loan_Ongoing
AS SELECT *
FROM loan
WHERE date_returned IS NULL ;

/* --- Emprunts actifs --- */
GO
CREATE OR ALTER VIEW Dashboard_User_Emprunts_Retards
AS SELECT *
FROM loan
WHERE DATEADD(DAY, 14, date_loaned) < GETDATE()
        AND date_returned IS NULL; ;

/* --- Retards --- */
GO
CREATE OR ALTER VIEW Dashboard_User_WaitingList_Pending
AS SELECT * FROM waiting_list WHERE status_reservation='PENDING';

/* --- Livres lus --- */
GO
CREATE OR ALTER VIEW Dashboard_User_Book_Read
AS
SELECT DISTINCT b.*
FROM book b
INNER JOIN book_copy c ON b.id = c.book_id
INNER JOIN loan l ON l.copy_id = c.id
WHERE l.date_returned IS NOT NULL;