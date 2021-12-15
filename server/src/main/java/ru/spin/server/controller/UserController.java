package ru.spin.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spin.server.dto.UserDto;
import ru.spin.server.entity.User;
import ru.spin.server.facade.UserFacade;
import ru.spin.server.service.UserService;
import ru.spin.server.validation.ResponseErrorValidation;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private UserService userService;
    private UserFacade userFacade;
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService,
                          UserFacade userFacade,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        User user = userService.getCurrentUser(principal);
        UserDto userDto = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userId") String id) {
        User user = userService.getUserById(Long.parseLong(id));
        UserDto userDto = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDto, principal);

        UserDto userUpdated = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
}
