
ALTER TABLE report
DROP FOREIGN KEY report_ibfk_3;

ALTER TABLE report
DROP COLUMN info_scam_id;
DROP TABLE info_scam;

ALTER TABLE report
ADD COLUMN status int,
ADD COLUMN info_description varchar(255),
ADD COLUMN reason varchar(255);

ALTER TABLE report
DROP FOREIGN KEY report_ibfk_2;
ALTER TABLE attachments
    ADD COLUMN news_id INT NULL;

ALTER TABLE attachments
    ADD CONSTRAINT fk_attachments_news
        FOREIGN KEY (news_id) REFERENCES news(id);

ALTER TABLE phone_scam
    CHANGE COLUMN info phone_number VARCHAR(20) NOT NULL
    COMMENT 'Số điện thoại bị báo cáo',
    ADD UNIQUE INDEX idx_phone_number (phone_number);

ALTER TABLE bank_scam
    CHANGE COLUMN info bank_account VARCHAR(34) NOT NULL
    COMMENT 'Số TK / số thẻ ngân hàng bị báo cáo',
    ADD INDEX idx_bank_account (bank_account);

ALTER TABLE url_scam
    MODIFY COLUMN info VARCHAR(512) NOT NULL
    COMMENT 'URL lừa đảo đã xác minh',
    ADD INDEX idx_url (info);


CREATE TABLE phone_scam_stats (
                                  phone_scam_id  INT PRIMARY KEY,
                                  verified_count INT NOT NULL DEFAULT 0,
                                  reasons_json   JSON NOT NULL,
                                  last_report_at TIMESTAMP NULL,
                                  CONSTRAINT fk_stats_phone
                                      FOREIGN KEY (phone_scam_id) REFERENCES phone_scam(id)
                                          ON DELETE CASCADE
);

CREATE TABLE bank_scam_stats (
                                 bank_scam_id   INT PRIMARY KEY,
                                 verified_count INT NOT NULL DEFAULT 0,
                                 reasons_json   JSON NOT NULL,
                                 last_report_at TIMESTAMP NULL,
                                 CONSTRAINT fk_stats_bank
                                     FOREIGN KEY (bank_scam_id) REFERENCES bank_scam(id)
                                         ON DELETE CASCADE
);

CREATE TABLE url_scam_stats (
                                url_scam_id    INT PRIMARY KEY,
                                verified_count INT NOT NULL DEFAULT 0,
                                reasons_json   JSON NOT NULL,
                                last_report_at TIMESTAMP NULL,
                                CONSTRAINT fk_stats_url
                                    FOREIGN KEY (url_scam_id) REFERENCES url_scam(id)
                                        ON DELETE CASCADE
);

ALTER TABLE phone_scam_stats ADD INDEX idx_phone_cnt (verified_count DESC);
ALTER TABLE phone_scam_stats ADD INDEX idx_phone_last (last_report_at DESC);

ALTER TABLE bank_scam_stats  ADD INDEX idx_bank_cnt  (verified_count DESC);
ALTER TABLE bank_scam_stats ADD INDEX idx_bank_last (last_report_at DESC);
ALTER TABLE url_scam_stats   ADD INDEX idx_url_cnt   (verified_count DESC);
ALTER TABLE url_scam_stats ADD INDEX idx_url_last (last_report_at DESC);

