CREATE TABLE google_calendars
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    calendar_id VARCHAR(160) NOT NULL,
    users_id    BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_user_id FOREIGN KEY (users_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
)