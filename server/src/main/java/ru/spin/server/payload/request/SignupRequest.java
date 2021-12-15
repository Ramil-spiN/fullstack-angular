package ru.spin.server.payload.request;

import lombok.Data;
import ru.spin.server.annotation.PasswordMatches;
import ru.spin.server.annotation.ValidEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@PasswordMatches //Кастомная аннотация
public class SignupRequest {
    @NotBlank(message = "Required field")
    private String username;
    @Email(message = "Invalid format")
    @ValidEmail //Кастомная аннотация
    private String email;
    @NotBlank(message = "Required field")
    private String firstname;
    @NotBlank(message = "Required field")
    private String lastname;
    @NotBlank(message = "Required field")
    @Size(min = 2)
    private String password;
    private String confirmPassword;
}
