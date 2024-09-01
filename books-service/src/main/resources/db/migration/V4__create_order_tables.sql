CREATE SEQUENCE order_id_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE order_item_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE orders
(
    id                        BIGINT DEFAULT NEXTVAL('order_id_seq') NOT NULL,
    order_number              text NOT NULL UNIQUE,
    user_id                   BIGINT NOT NULL REFERENCES users (id),
    customer_name             text NOT NULL,
    customer_email            text NOT NULL,
    customer_phone            text NOT NULL,
    delivery_address_line1    text NOT NULL,
    delivery_address_line2    text,
    delivery_address_city     text NOT NULL,
    delivery_address_state    text NOT NULL,
    delivery_address_zip_code text NOT NULL,
    delivery_address_country  text NOT NULL,
    status                    text NOT NULL,
    comments                  text,
    created_at                TIMESTAMP,
    updated_at                TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT status_check CHECK (status IN ('NEW', 'IN_PROCESS', 'DELIVERY_IN_PROGRESS', 'DELIVERED', 'CANCELLED', 'ERROR'))
);

CREATE TABLE order_items (
    id BIGINT DEFAULT NEXTVAL('order_item_id_seq') PRIMARY KEY,
    code VARCHAR(255) NOT NULL REFERENCES books (code),
    name TEXT NOT NULL,
    price NUMERIC NOT NULL,
    quantity INTEGER NOT NULL,
    order_id BIGINT NOT NULL REFERENCES orders (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
