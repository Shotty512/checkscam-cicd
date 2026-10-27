ALTER TABLE news
    DROP COLUMN url,
    DROP COLUMN created_at,
    ADD COLUMN name VARCHAR(255) NOT NULL,
    ADD COLUMN short_description TEXT NULL,
    ADD COLUMN content TEXT NULL;
