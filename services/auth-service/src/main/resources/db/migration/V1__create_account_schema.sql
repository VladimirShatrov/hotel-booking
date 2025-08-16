CREATE SCHEMA IF NOT EXISTS account_schema;

-- Create users table
CREATE TABLE account_schema.users (
                                        id uuid PRIMARY KEY,
                                        email VARCHAR(255) NOT NULL UNIQUE,
                                        password TEXT NOT NULL,
                                        credentials_expired BOOLEAN,
                                        enabled BOOLEAN,
                                        locked BOOLEAN
);

-- Create roles table
CREATE TABLE account_schema.roles (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL UNIQUE
);

-- Create users_roles table
CREATE TABLE account_schema.users_roles (
                                     roles_id uuid,
                                     users_id uuid,
                                     CONSTRAINT fk_role_id FOREIGN KEY (roles_id) REFERENCES account_schema.roles(id),
                                     CONSTRAINT fk_user_id FOREIGN KEY (users_id) REFERENCES account_schema.users(id)
);