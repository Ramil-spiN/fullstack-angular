package ru.spin.server.facade;

import org.springframework.stereotype.Component;
import ru.spin.server.dto.BricksetDto;
import ru.spin.server.entity.Brickset;

@Component
public class BricksetFacade {
    public BricksetDto bricksetToBricksetDto(Brickset brickset) {
        BricksetDto bricksetDto = new BricksetDto();
        bricksetDto.setId(brickset.getId());
        bricksetDto.setUsername(brickset.getUser().getUsername());
        bricksetDto.setTitle(brickset.getTitle());
        bricksetDto.setCaption(brickset.getCaption());
        bricksetDto.setInventory(brickset.getInventory());
        bricksetDto.setLikes(brickset.getLikes());
        bricksetDto.setLikedUsers(brickset.getLikedUsers());
        return bricksetDto;
    }
}
