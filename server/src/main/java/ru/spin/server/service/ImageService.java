package ru.spin.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.spin.server.entity.Brickset;
import ru.spin.server.entity.ImageModel;
import ru.spin.server.entity.User;
import ru.spin.server.exception.ImageNotFoundException;
import ru.spin.server.repository.BricksetRepository;
import ru.spin.server.repository.ImageRepository;
import ru.spin.server.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {
    public static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private ImageRepository imageRepository;
    private UserRepository userRepository;
    private BricksetRepository bricksetRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository, BricksetRepository bricksetRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.bricksetRepository = bricksetRepository;
    }

    public ImageModel getUserImage(Principal principal) {
        User user = getUserByPrincipal(principal);
        ImageModel imageModel = imageRepository.findImageModelByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getBricksetImage(Long bricksetId) {
        ImageModel imageModel = imageRepository.findImageModelByBricksetId(bricksetId)//.orElse(null);
                .orElseThrow(() -> new ImageNotFoundException("Image not found for brickset"));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getBricksetUserImage(Long bricksetId) {
        Brickset brickset = bricksetRepository.getById(bricksetId);

        ImageModel imageModel = imageRepository.findImageModelByUserId(brickset.getUser().getId())//.orElse(null);
                .orElseThrow(() -> new ImageNotFoundException("Image of user not found for brickset"));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = getUserByPrincipal(principal);
        LOGGER.info("Uploading image profile for {}", user.getUsername());

        ImageModel userProfileImage = imageRepository.findImageModelByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));

        return imageRepository.save(imageModel);
    }

    public ImageModel uploadImageToBrickset(MultipartFile file, Principal principal, Long bricksetId) throws IOException {
        User user = getUserByPrincipal(principal);

        Brickset brickset = user.getBricksets().stream()
                .filter(b -> b.getId().equals(bricksetId))
                .collect(toSingleBricksetCollector());
        LOGGER.info("Uploading image to {} for {}", brickset.getTitle(), user.getUsername());

        ImageModel bricksetImage = imageRepository.findImageModelByBricksetId(brickset.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(bricksetImage)) {
            imageRepository.delete(bricksetImage);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setBricksetId(brickset.getId());
        imageModel.setName(file.getOriginalFilename());
        imageModel.setImageBytes(compressBytes(file.getBytes()));

        return imageRepository.save(imageModel);
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            LOGGER.error("Cannot compress bytes");
        }
        System.out.println("Compressed image byte size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch(IOException | DataFormatException e){
            LOGGER.error("Cannot decompress bytes");
        }
        return outputStream.toByteArray();
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    private <T> Collector<T, ?, T> toSingleBricksetCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
