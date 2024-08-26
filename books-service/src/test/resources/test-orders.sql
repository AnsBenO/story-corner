TRUNCATE TABLE orders CASCADE;
ALTER SEQUENCE order_id_seq RESTART WITH 100;
ALTER SEQUENCE order_item_id_seq RESTART WITH 100;

INSERT INTO orders (
                        id,
                        order_number,
                        username,
                        customer_name,
                        customer_email,
                        customer_phone,
                        delivery_address_line1,
                        delivery_address_line2,
                        delivery_address_city,
                        delivery_address_state,
                        delivery_address_zip_code,
                        delivery_address_country,
                        status,
                        comments
                  ) VALUES
(1, 'order-123', 'testuser', 'testuser', 'testuser@gmail.com', '11111111', '123 Main St', 'Apt 1', 'Dallas', 'TX', '75001', 'USA', 'NEW', null),
(2, 'order-456', 'testuser', 'Prasad', 'prasad@gmail.com', '2222222', '123 Main St', 'Apt 1', 'Hyderabad', 'TS', '500072', 'India', 'NEW', null)
;

INSERT INTO order_items(order_id, code, name, price, quantity) VALUES
(1, 'P100', 'The Hunger Games', 34.0, 2),
(1, 'P101', 'To Kill a Mockingbird', 45.40, 1),
(2, 'P102', 'The Chronicles of Narnia', 44.50, 1)
;

-- Clear out any existing test data to avoid conflicts
DELETE FROM tokens;
DELETE FROM users ;

-- Now proceed with the insert, including created_at and updated_at fields
INSERT INTO users (username, first_name, last_name, password, email, phone, role, country, created_at, updated_at)
VALUES (
  'testuser',
  'John',
  'Doe',
  '$2a$10$DowJTO.e/l8Vs6t1L9x/mOIuK9nDToZ7j1r0jZD3NAdD5kJROUw7O',  -- Assume this is a valid bcrypt hashed password
  'testuser@test.com',
  '0345678903',
  'CUSTOMER',
  'USA',
  NOW(),  -- Use the current timestamp for created_at
  NOW()   -- Use the current timestamp for updated_at
);