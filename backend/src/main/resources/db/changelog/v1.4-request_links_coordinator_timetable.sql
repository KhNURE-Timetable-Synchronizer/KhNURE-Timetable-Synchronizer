-- liquibase formatted sql
-- changeset request_links_coordinator_timetable:1

CREATE TABLE request_links_coordinator_timetable
(
    id                   BIGINT          NOT NULL AUTO_INCREMENT,
    contact_account      VARCHAR(160),
    status_request       VARCHAR(60)     NOT NULL,
    khnure_timetables_id BIGINT          NOT NULL,
    users_id             BIGINT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_request_links_coordinator_timetable_user_id FOREIGN KEY (users_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_request_links_coordinator_timetable_khnure_timetables_id FOREIGN KEY (khnure_timetables_id) REFERENCES khnure_timetables(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- changeset request_links_coordinator_timetable:2
ALTER TABLE google_calendars DROP CONSTRAINT fk_khnure_timetables_id;

ALTER TABLE google_calendars
    ADD CONSTRAINT fk_google_calendars_khnure_timetables_id FOREIGN KEY (khnure_timetables_id) REFERENCES khnure_timetables(id)
        ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE google_calendars DROP CONSTRAINT fk_user_user_id;

ALTER TABLE google_calendars
    ADD CONSTRAINT fk_google_calendars_user_id FOREIGN KEY (khnure_timetables_id) REFERENCES khnure_timetables(id)
        ON DELETE CASCADE ON UPDATE CASCADE;