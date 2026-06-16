-- Run this once in MySQL if your integration_logs table already exists.
-- Hibernate ddl-auto=update may not convert existing TEXT/VARCHAR columns to LONGTEXT automatically.

ALTER TABLE integration_logs
MODIFY COLUMN request_payload LONGTEXT NULL,
MODIFY COLUMN response_payload LONGTEXT NULL;
