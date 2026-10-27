ALTER TABLE report
DROP FOREIGN KEY report_ibfk_1;

ALTER TABLE report
DROP COLUMN created_at,
DROP COLUMN news_id,
DROP COLUMN created_by;

ALTER TABLE report
ADD COLUMN email_author_report VARCHAR(255) DEFAULT NULL,
ADD COLUMN date_report DATETIME DEFAULT NULL;

ALTER TABLE report
    ADD COLUMN phone_scam_id INT NULL,
    ADD COLUMN bank_scam_id INT NULL,
    ADD COLUMN url_scam_id INT NULL;


ALTER TABLE report
    ADD CONSTRAINT fk_report_phone_scam
        FOREIGN KEY (phone_scam_id) REFERENCES phone_scam(id)
            ON DELETE SET NULL,

    ADD CONSTRAINT fk_report_bank_scam
        FOREIGN KEY (bank_scam_id) REFERENCES bank_scam(id)
        ON DELETE SET NULL,

    ADD CONSTRAINT fk_report_url_scam
        FOREIGN KEY (url_scam_id) REFERENCES url_scam(id)
        ON DELETE SET NULL;

DROP TABLE IF EXISTS report_phone_scams;
DROP TABLE IF EXISTS report_bank_scams;
DROP TABLE IF EXISTS report_url_scams;

CREATE INDEX idx_report_phone_scam ON report(phone_scam_id);
CREATE INDEX idx_report_bank_scam  ON report(bank_scam_id);
CREATE INDEX idx_report_url_scam   ON report(url_scam_id);

