DELIMITER $$

CREATE TRIGGER after_purchase_insert
    AFTER INSERT ON Purchases
    FOR EACH ROW
BEGIN
    DELETE FROM Carted WHERE cID = NEW.cID;
    END$$

    DELIMITER ;