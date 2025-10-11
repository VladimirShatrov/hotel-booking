CREATE TABLE IF NOT EXISTS room_schema.hotels (
                                                  id uuid PRIMARY KEY,
                                                  name VARCHAR(255) NOT NULL,
                                                  latitude double precision,
                                                  longitude double precision
);

ALTER TABLE room_schema.rooms RENAME COLUMN name TO number;
ALTER TABLE room_schema.rooms ADD COLUMN hotel_id uuid;
ALTER TABLE room_schema.rooms ADD COLUMN amenities jsonb DEFAULT '[]'::jsonb;

ALTER TABLE room_schema.rooms
    ADD CONSTRAINT fk_rooms_hotels FOREIGN KEY (hotel_id) REFERENCES room_schema.hotels(id);
