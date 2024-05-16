-- liquibase formatted sql
-- changeset change-user-calendar-add-khnure_timetables:1


CREATE TABLE khnure_timetables
(
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255) NOT NULL,
    khnure_timetable_id VARCHAR(160) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE google_calendars
DROP COLUMN name;

ALTER TABLE google_calendars
    ADD COLUMN khnure_timetables_id BIGINT;

ALTER TABLE google_calendars
    ADD CONSTRAINT fk_khnure_timetables_id FOREIGN KEY (khnure_timetables_id) REFERENCES khnure_timetables(id)
        ON DELETE CASCADE ON UPDATE CASCADE;