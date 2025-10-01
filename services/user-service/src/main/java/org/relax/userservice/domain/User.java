package org.relax.userservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Builder
@Table(name = "users")
public class User {

    @Id
    private UUID id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    @NotBlank
    private String email;

    @Column(name = "username")
    private String username;

    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;

        UserBuilder() {
        }

        public UserBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        public UserBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder username(final String username) {
            this.username = username;
            return this;
        }

        public User build() {
            return new User(this.id, this.firstName, this.lastName, this.email, this.username);
        }

        public String toString() {
            String var10000 = String.valueOf(this.id);
            return "User.UserBuilder(id=" + var10000 + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", email=" + this.email + ", username=" + this.username + ")";
        }
    }
}
