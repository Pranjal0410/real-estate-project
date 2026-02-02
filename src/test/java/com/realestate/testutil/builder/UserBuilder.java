package com.realestate.testutil.builder;

import com.realestate.model.entity.User;

public class UserBuilder {

    private Long id = 1L;
    private String username = "testuser";
    private String email = "test@example.com";
    private String password = "password123";
    private String firstName = "Test";
    private String lastName = "User";
    private String phoneNumber = "+1234567890";
    private User.UserRole role = User.UserRole.INVESTOR;
    private Boolean isEnabled = true;

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder asInvestor() {
        this.role = User.UserRole.INVESTOR;
        return this;
    }

    public UserBuilder asAdmin() {
        this.role = User.UserRole.ADMIN;
        return this;
    }

    public UserBuilder asAnalyst() {
        this.role = User.UserRole.ANALYST;
        return this;
    }

    public UserBuilder disabled() {
        this.isEnabled = false;
        return this;
    }

    public User build() {
        User user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .role(role)
                .isEnabled(isEnabled)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
        user.setId(id);
        return user;
    }
}
