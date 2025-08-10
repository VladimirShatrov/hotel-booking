CREATE SCHEMA IF NOT EXISTS places_schema;

-- Create location table
CREATE TABLE places_schema.location (
                                        id BIGSERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
                                        street VARCHAR(255) NOT NULL,
                                        building_number VARCHAR(255) NOT NULL,
                                        city VARCHAR(255) NOT NULL,
                                        postal_code VARCHAR(5),
                                        email VARCHAR(255) NOT NULL,
                                        phone_number VARCHAR(255)
);

-- Create floor table
CREATE TABLE places_schema.floor (
                                     id BIGSERIAL PRIMARY KEY,
                                     floor_number INTEGER NOT NULL,
                                     location_id BIGINT NOT NULL,
                                     CONSTRAINT fk_location_id FOREIGN KEY (location_id) REFERENCES places_schema.location(id)
);

-- Create space table
CREATE TABLE places_schema.space (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(255),
                                     seating_capacity INTEGER,
                                     floor_id BIGINT NOT NULL,
                                     space_type VARCHAR(31) NOT NULL,
                                     CONSTRAINT fk_floor_id FOREIGN KEY (floor_id) REFERENCES places_schema.floor(id)
);

-- Create chill_space table
CREATE TABLE places_schema.chill_space (
                                           id BIGINT PRIMARY KEY,
                                           number_of_sockets INTEGER,
                                           CONSTRAINT fk_chill_space_id FOREIGN KEY (id) REFERENCES places_schema.space(id)
);

-- Create kitchen table
CREATE TABLE places_schema.kitchen (
                                       id BIGINT PRIMARY KEY,
                                       has_coffee_machine BOOLEAN,
                                       has_fridge BOOLEAN,
                                       has_microwave BOOLEAN,
                                       CONSTRAINT fk_kitchen_id FOREIGN KEY (id) REFERENCES places_schema.space(id)
);

-- Create meeting_room table
CREATE TABLE places_schema.meeting_room (
                                            id BIGINT PRIMARY KEY,
                                            has_drawing_board BOOLEAN,
                                            has_projector BOOLEAN,
                                            CONSTRAINT fk_meeting_room_id FOREIGN KEY (id) REFERENCES places_schema.space(id)
);

-- Create work_space table
CREATE TABLE places_schema.work_space (
                                          id BIGINT PRIMARY KEY,
                                          has_computer BOOLEAN,
                                          number_of_monitors INTEGER,
                                          number_of_sockets INTEGER,
                                          CONSTRAINT fk_work_space_id FOREIGN KEY (id) REFERENCES places_schema.space(id)
);