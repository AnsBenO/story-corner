CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE users (
    id BIGINT DEFAULT         NEXTVAL('user_id_seq') PRIMARY KEY,
    username                  VARCHAR(255) UNIQUE NOT NULL,
    password                  VARCHAR(255) NOT NULL,
    role VARCHAR(50)          NOT NULL CHECK (role IN ('CUSTOMER', 'ADMIN'))
);
