CREATE SCHEMA IF NOT EXISTS order_schema;

CREATE TABLE order_schema.bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    space_id BIGINT NOT NULL,
    floor_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
