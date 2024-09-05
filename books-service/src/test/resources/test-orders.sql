DELETE FROM order_items; 
DELETE FROM orders;      
DELETE FROM tokens;
DELETE FROM notifications;
DELETE FROM users;

INSERT INTO users (id, username, first_name, last_name, password, email, phone, role, country, created_at, updated_at)
VALUES (
  1,
  'testuser',
  'John',
  'Doe',
  '$2a$10$DowJTO.e/l8Vs6t1L9x/mOIuK9nDToZ7j1r0jZD3NAdD5kJROUw7O',  
  'testuser@test.com',
  '0345678903',
  'CUSTOMER',
  'USA',
  NOW(),  -- current timestamp for created_at
  NOW()   -- current timestamp for updated_at
);

TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE order_items RESTART IDENTITY CASCADE;

ALTER SEQUENCE order_id_seq RESTART WITH 100;
ALTER SEQUENCE order_item_id_seq RESTART WITH 100;

INSERT INTO orders (
                        id,
                        order_number,
                        user_id,
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
(1, 'order-123', 1, 'testuser', 'testuser@gmail.com', '11111111', '123 Main St', 'Apt 1', 'Dallas', 'TX', '75001', 'USA', 'NEW', null),
(2, 'order-456', 1, 'testuser', 'testuser@gmail.com', '11111111', '123 Main St', 'Apt 1', 'Dallas', 'TX', '75001', 'USA', 'NEW', null)
;

INSERT INTO order_items(order_id, code, name, price, quantity) VALUES
(1, 'P100', 'The Hunger Games', 34.0, 2),
(1, 'P101', 'To Kill a Mockingbird', 45.40, 1),
(2, 'P102', 'The Chronicles of Narnia', 44.50, 1)
;


