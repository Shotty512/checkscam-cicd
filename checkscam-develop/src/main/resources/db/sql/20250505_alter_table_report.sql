ALTER TABLE report
    ADD COLUMN type INT NULL,
    ADD COLUMN id_scam_type_before_handle INT NULL;
ALTER TABLE report
drop
foreign key fk_report_bank_scam,
     drop
foreign key fk_report_phone_scam,
        drop
foreign key fk_report_url_scam,
DROP
COLUMN phone_scam_id,
DROP
COLUMN bank_scam_id,
DROP
COLUMN url_scam_id;


