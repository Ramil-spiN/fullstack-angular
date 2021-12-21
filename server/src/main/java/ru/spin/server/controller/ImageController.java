package ru.spin.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.spin.server.entity.ImageModel;
import ru.spin.server.payload.response.MessageResponse;
import ru.spin.server.service.ImageService;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {
    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/profile-image")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal) {
        ImageModel imageModel = imageService.getUserImage(principal);
        return ResponseEntity.ok(imageModel);
    }

    @GetMapping("/{bricksetId}/image")
    public ResponseEntity<ImageModel> getImageForBrickset(@PathVariable("bricksetId") String bricksetId) {
        ImageModel imageModel = imageService.getBricksetImage(Long.parseLong(bricksetId));
        return ResponseEntity.ok(imageModel);
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageService.uploadImageToUser(file, principal);

        return ResponseEntity.ok(new MessageResponse("File uploaded successfully"));
    }

    @PostMapping("{bricksetId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToBrickset(@RequestParam("file") MultipartFile file,
                                                                 @PathVariable("bricksetId") String bricksetId,
                                                                 Principal principal) throws IOException {
        imageService.uploadImageToBrickset(file, principal, Long.parseLong(bricksetId));

        return ResponseEntity.ok(new MessageResponse("File uploaded successfully"));
    }
}
