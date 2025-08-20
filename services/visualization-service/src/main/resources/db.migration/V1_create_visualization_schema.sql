CREATE SCHEMA IF NOT EXISTS visualization_schema;

CREATE TABLE IF NOT EXISTS visualization_schema.floor_visualization (
id BIGSERIAL PRIMARY KEY,
floor_id BIGINT NOT NULL,
image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS visualization_schema.space_coordinates (
 id BIGSERIAL PRIMARY KEY,
 space_id BIGINT NOT NULL,
 x INTEGER NOT NULL,
 y INTEGER NOT NULL,
 floor_image_id BIGINT NOT NULL,
 CONSTRAINT fk_space_coordinates_floor_visualization
     FOREIGN KEY (floor_image_id)
         REFERENCES floor_visualization(id)
         ON DELETE CASCADE
);
