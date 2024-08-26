CREATE SEQUENCE token_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE tokens (
      id BIGINT DEFAULT NEXTVAL('token_id_seq') PRIMARY KEY,
      token TEXT NOT NULL UNIQUE,
      user_id BIGINT NOT NULL REFERENCES users (id),
      token_type VARCHAR(15) NOT NULL,
      revoked BOOLEAN DEFAULT FALSE,
      usage_count INTEGER DEFAULT 0,
      CONSTRAINT token_type_check CHECK (token_type IN ('ACCESS_TOKEN', 'REFRESH_TOKEN'))
);

--  trigger function that will revoke the token if it's a refresh token and has been used 3 times
CREATE OR REPLACE FUNCTION revoke_token_if_used_three_times()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.token_type = 'REFRESH_TOKEN' AND NEW.usage_count >= 3) THEN
        NEW.revoked := TRUE;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger that fires before any UPDATE on the tokens table
CREATE TRIGGER trigger_revoke_token
BEFORE UPDATE ON tokens
FOR EACH ROW
EXECUTE FUNCTION revoke_token_if_used_three_times();