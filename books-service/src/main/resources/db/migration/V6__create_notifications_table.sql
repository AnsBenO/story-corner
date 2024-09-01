-- Create sequence for notification IDs
CREATE SEQUENCE notification_id_seq
START WITH 1
INCREMENT BY 50;

-- Create notifications table
CREATE TABLE notifications (
    id BIGINT DEFAULT NEXTVAL('notification_id_seq') PRIMARY KEY,
    user_id BIGINT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Index on user_id for faster queries
CREATE INDEX idx_user_id ON notifications(user_id);
