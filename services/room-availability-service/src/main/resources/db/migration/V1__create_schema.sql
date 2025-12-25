CREATE SCHEMA IF NOT EXISTS availability_schema;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE availability_schema.room(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY
);

CREATE TYPE room_status as ENUM('BOOKED', 'BLOCKED', 'MAINTENANCE');

CREATE TABLE availability_schema.room_availability(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    room_id UUID NOT NULL,
    status room_status NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_room_availability_room
        FOREIGN KEY (room_id)
        REFERENCES room(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_room_availability_room_id
    ON availability_schema.room_availability (room_id);

CREATE INDEX idx_room_availability_date_range
    ON availability_schema.room_availability (start_date, end_date);