/* ------------------------------------------------------------------- */
/* --- Procédure stockée : recalcule les rangs pour un livre donné --- */
/* ------------------------------------------------------------------- */
GO

CREATE OR ALTER PROCEDURE SP_recalculate_rank @bookId INT AS
BEGIN
UPDATE waiting_list
    SET rank_number = subquery.new_rank
    FROM waiting_list AS w
    LEFT JOIN (
        SELECT id, ROW_NUMBER() OVER (PARTITION BY book_id ORDER BY date_added ASC) AS new_rank
        FROM waiting_list
        WHERE book_id = @bookId AND status_reservation = 'PENDING'
    ) AS subquery ON w.id = subquery.id
WHERE w.book_id = @bookId;
END;

/* ------------------------------------------------------------------- */
/* ---	Trigger sur la table WAITING_LIST                          --- */
/* ---      * INSERT : une nouvelle réservation est ajoutée        --- */
/* ---	    * UPDATE : une réservation est annulée                 --- */
/* ------------------------------------------------------------------- */
GO

CREATE OR ALTER TRIGGER TRG_resa_rank ON waiting_list
FOR INSERT, UPDATE AS
BEGIN
    DECLARE @bookId INT;
    SELECT @bookId = book_id FROM inserted;
    -- En cas d'UPDATE, on ne recalcule que si status_reservation a changé
    IF UPDATE(status_reservation)   -- cas UPDATE : le status a changé
       OR NOT EXISTS (SELECT 1 FROM deleted)  -- cas INSERT : deleted est vide
    BEGIN
        EXEC SP_recalculate_rank @bookId;
    END
END;

/* ------------------------------------------------------------------- */
/* ---	Trigger sur la table LOAN                                  --- */
/* ---      * UPDATE : un retour est enregistré                    --- */
/* --- Déclenchement si une date de retour est enregistrée !       --- */
/* ------------------------------------------------------------------- */
GO

CREATE OR ALTER TRIGGER TRG_loan_rank ON loan
FOR UPDATE AS
BEGIN
    DECLARE @copyId INT;
    SELECT @copyId = copy_id FROM inserted;
    DECLARE @bookId INT;
    SELECT TOP 1 @bookId = book_id FROM book_copy WHERE id = @copyId ;
    -- En cas d'UPDATE, on ne recalcule que si status_reservation a changé
    IF UPDATE(date_returned)
    BEGIN
        UPDATE book_copy SET is_available = 1 WHERE id=@copyId AND state != 'RETIRED';
        EXEC SP_recalculate_rank @bookId;
    END
END;

/* UPDATE book_copy SET is_available=0 WHERE book_id=85; */
/* 288923619-6 */
