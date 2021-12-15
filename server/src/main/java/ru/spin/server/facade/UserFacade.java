package ru.spin.server.facade;

import org.springframework.stereotype.Component;
import ru.spin.server.dto.UserDto;
import ru.spin.server.entity.User;

@Component
public class UserFacade {
    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setBio(user.getBio());
        return userDto;
    }
}
