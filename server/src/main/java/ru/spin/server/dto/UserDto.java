package ru.spin.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    private long id;
    private String username;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String bio;
}
