-- Create trigger function to calculate total price as product of price*quantity
CREATE OR REPLACE FUNCTION calculate_purchase_total()
RETURNS TRIGGER AS $function$
BEGIN
    -- Get the product price
    SELECT price INTO NEW.total FROM products WHERE code = NEW.product_code;
    
    -- Calculate the total
    NEW.total := NEW.total * NEW.quantity;
    
    RETURN NEW;
END;
$function$ LANGUAGE plpgsql;

-- Create trigger to execute the function before insert
DROP TRIGGER IF EXISTS before_purchase_insert ON purchases;
CREATE TRIGGER before_purchase_insert
BEFORE INSERT ON purchases
FOR EACH ROW
EXECUTE FUNCTION calculate_purchase_total(); 