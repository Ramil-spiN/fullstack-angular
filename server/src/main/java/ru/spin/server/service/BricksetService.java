package ru.spin.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.spin.server.dto.BricksetDto;
import ru.spin.server.entity.Brickset;
import ru.spin.server.entity.ImageModel;
import ru.spin.server.entity.User;
import ru.spin.server.exception.BricksetNotFoundException;
import ru.spin.server.repository.BricksetRepository;
import ru.spin.server.repository.ImageRepository;
import ru.spin.server.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class BricksetService {
    public static final Logger LOGGER = LoggerFactory.getLogger(BricksetService.class);

    private BricksetRepository bricksetRepository;
    private UserRepository userRepository;
    private ImageRepository imageRepository;

    @Autowired
    public BricksetService(BricksetRepository bricksetRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.bricksetRepository = bricksetRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public List<Brickset> getAllBricksets() {
        return bricksetRepository.findAllByOrderByCreateDateDesc();
    }

    public Brickset getBricksetByIdAndUser(Long id, Principal principal) {
        User user = getUserByPrincipal(principal);
        return bricksetRepository.findBricksetByIdAndUser(id, user)
                .orElseThrow(() -> new BricksetNotFoundException("Brickset not found for " + user.getUsername()));
    }

    public List<Brickset> getAllBricksetsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return bricksetRepository.findAllByUserOrderByCreateDateDesc(user);
    }

    public Brickset createBrickset(BricksetDto bricksetDto, Principal principal) {
        User user = getUserByPrincipal(principal);
        Brickset brickset = new Brickset();
        brickset.setUser(user);
        brickset.setTitle(bricksetDto.getTitle());
        brickset.setCaption(bricksetDto.getCaption());
        brickset.setInventory(bricksetDto.getInventory());
        brickset.setLikes(0);

        LOGGER.info("Saving brickset for {}", user.getUsername());
        return bricksetRepository.save(brickset);
    }

    public Brickset updateBrickset(Long id, BricksetDto bricksetDto, Principal principal) {
        Brickset brickset = getBricksetByIdAndUser(id, principal);
        brickset.setTitle(bricksetDto.getTitle());
        brickset.setCaption(bricksetDto.getCaption());
        brickset.setInventory(bricksetDto.getInventory());
        LOGGER.info("Updating brickset {}", bricksetDto.getTitle());
        return bricksetRepository.save(brickset);
    }

    public Brickset likeBrickset(Long id, String username) {
        Brickset brickset = bricksetRepository.findById(id)
                .orElseThrow(() -> new BricksetNotFoundException("Brickset not found"));

        Optional<String> likedUser = brickset.getLikedUsers().stream()
                .filter(u -> u.equals(username))
                .findAny();

        if (likedUser.isPresent()) {
            brickset.setLikes(brickset.getLikes() - 1);
            brickset.getLikedUsers().remove(username);
        } else {
            brickset.setLikes(brickset.getLikes() + 1);
            brickset.getLikedUsers().add(username);
        }

        return bricksetRepository.save(brickset);
    }

    public void deleteBrickset(Long id, Principal principal) {
        Brickset brickset = getBricksetByIdAndUser(id, principal);
        Optional<ImageModel> imageModel = imageRepository.findImageModelByBricksetId(brickset.getId());
        bricksetRepository.delete(brickset);
        imageModel.ifPresent(imageRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
