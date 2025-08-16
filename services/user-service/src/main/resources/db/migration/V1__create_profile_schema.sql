CREATE SCHEMA IF NOT EXISTS profile_schema;

-- Create departments table
CREATE TABLE profile_schema.departments (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE profile_schema.users (
                                      id uuid PRIMARY KEY,
                                      email VARCHAR(255) NOT NULL UNIQUE,
                                      firstName VARCHAR(255),
                                      lastName VARCHAR(255),
                                      jobTitle VARCHAR(255),
                                      department_id BIGINT REFERENCES profile_schema.departments(id)

);
