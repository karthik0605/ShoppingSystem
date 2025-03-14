DELIMITER $$

CREATE TRIGGER after_purchase_insert
    AFTER INSERT ON Purchases
    FOR EACH ROW
BEGIN
    -- Delete the cart entries for the customer that made the purchase
    DELETE FROM Carted WHERE cID = NEW.cID;
    END$$

    DELIMITER ;