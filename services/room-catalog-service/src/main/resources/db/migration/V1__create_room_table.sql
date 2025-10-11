CREATE SCHEMA IF NOT EXISTS room_schema;

CREATE TABLE room_schema.rooms (
                                   id uuid PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL UNIQUE,
                                   capacity INT,
                                   location VARCHAR(255)
);
