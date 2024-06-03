-- liquibase formatted sql
-- changeset permission-table-for-editing-links-for-timetable:add-table

CREATE TABLE links_coordinator_has_permission_to_khnure_timetable
(
    id                   BIGINT          NOT NULL AUTO_INCREMENT,
    khnure_timetable_id BIGINT          NOT NULL,
    user_id             BIGINT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_links_coordinator_user_id FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_khnure_timetables_id FOREIGN KEY (khnure_timetable_id) REFERENCES khnure_timetables(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);