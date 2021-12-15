package ru.spin.server.payload.response;

import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private String message;

    public InvalidLoginResponse() {
        this.message = "Invalid username or password";
    }

//    private String username;
//    private String password;
//
//    public InvalidLoginResponse() {
//        this.username = "Invalid username";
//        this.password = "Invalid password";
//    }
}
