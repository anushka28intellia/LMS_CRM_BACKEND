--liquibase formatted sql

-- Brief §3.8.2: "Handle with care. The user types a reason. Permissions control who sees the reason."
-- The reason field on Person and Entity becomes Sensitive, so only users whose role holds CUSTOM_FIELD_SENSITIVE
-- see or change it (ADMIN by default; an administrator can grant it to other roles). Values already stored are kept.

--changeset privateinvest:009-handle-with-care-sensitive
UPDATE custom_field_def
SET sensitive = TRUE, updated_at = now(), updated_by = 'seed', version = version + 1
WHERE api_name = 'handleWithCare' AND object_api_name IN ('person', 'entity') AND NOT sensitive;
