-- liquibase formatted sql
-- changeset add-jwt-refresh-token:1

ALTER TABLE users
    ADD COLUMN jwt_refresh_token_expired_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE users
    ADD COLUMN jwt_refresh_token TEXT NOT NULL;
