DROP SCHEMA IF EXISTS blogs CASCADE;

CREATE SCHEMA IF NOT EXISTS blogs;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS blogs.users
(
    user_id         UUID PRIMARY KEY             DEFAULT uuid_generate_v4(),
    username        VARCHAR(50) UNIQUE  NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    first_name      VARCHAR(50)         NOT NULL,
    last_name       VARCHAR(50)         NOT NULL,
    image_url       TEXT                         DEFAULT NULL,
    hashed_password VARCHAR(255)        NOT NULL,
    email_verified  BOOLEAN             NOT NULL DEFAULT FALSE,
    is_blocked      BOOLEAN             NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS blogs.roles
(
    role_id   SERIAL PRIMARY KEY,
    role_code VARCHAR(10) UNIQUE NOT NULL,
    role_name VARCHAR(50) DEFAULT NULL,
    role_desc TEXT        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS blogs.permissions
(
    permission_id   SERIAL PRIMARY KEY,
    permission_code VARCHAR(30) UNIQUE NOT NULL,
    permission_name VARCHAR(50) DEFAULT NULL,
    permission_desc TEXT        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS blogs.user_role
(
    user_id UUID    NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES blogs.users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES blogs.roles (role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS blogs.role_permission
(
    role_id       INTEGER NOT NULL,
    permission_id INTEGER NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES blogs.roles (role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES blogs.permissions (permission_id) ON DELETE CASCADE
);

-- CREATE TABLE IF NOT EXISTS blogs.tokens
-- (
--     token_id   SERIAL PRIMARY KEY,
--     user_id    UUID                     NOT NULL,
--     token_type VARCHAR(10)              NOT NULL,
--     value      VARCHAR(255)             NOT NULL,
--     is_revoked BOOLEAN                  NOT NULL DEFAULT FALSE,
--     expired_at TIMESTAMP WITH TIME ZONE NOT NULL,
--     ip_address VARCHAR(50)              NOT NULL,
--     CONSTRAINT valid_token_type CHECK (token_type IN ('REFRESH', 'VERIFIED')),
--     FOREIGN KEY (user_id) REFERENCES blogs.users (user_id) ON DELETE CASCADE
-- );