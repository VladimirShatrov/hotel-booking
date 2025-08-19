ALTER TABLE profile_schema.departments RENAME COLUMN name To title;

ALTER TABLE profile_schema.departments ADD CONSTRAINT departments_title_unique UNIQUE (title);

ALTER TABLE profile_schema.departments DROP CONSTRAINT IF EXISTS departments_name_unique;