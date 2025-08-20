CREATE SCHEMA IF NOT EXISTS visualization_schema;



CREATE TABLE IF NOT EXISTS floor_visualization (
id BIGSERIAL PRIMARY KEY,
floor_id BIGINT NOT NULL,
image_url VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS space_coordinates (
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

CREATE INDEX IF NOT EXISTS idx_floor_visualization_floor_id ON floor_visualization(floor_id);
CREATE INDEX IF NOT EXISTS idx_space_coordinates_space_id ON space_coordinates(space_id);
CREATE INDEX IF NOT EXISTS idx_space_coordinates_floor_image_id ON space_coordinates(floor_image_id);