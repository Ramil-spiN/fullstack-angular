package ru.spin.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.spin.server.dto.BricksetDto;
import ru.spin.server.entity.Brickset;
import ru.spin.server.facade.BricksetFacade;
import ru.spin.server.payload.response.MessageResponse;
import ru.spin.server.service.BricksetService;
import ru.spin.server.validation.ResponseErrorValidation;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brickset")
@CrossOrigin
public class BricksetController {
    private BricksetService bricksetService;
    private BricksetFacade bricksetFacade;
    private ResponseErrorValidation responseErrorValidation;

    @Autowired
    public BricksetController(BricksetService bricksetService,
                              BricksetFacade bricksetFacade,
                              ResponseErrorValidation responseErrorValidation) {
        this.bricksetService = bricksetService;
        this.bricksetFacade = bricksetFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BricksetDto>> getAllBricksets() {
        List<BricksetDto> bricksetDtoList = bricksetService.getAllBricksets().stream()
                .map(bricksetFacade::bricksetToBricksetDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bricksetDtoList, HttpStatus.OK);
    }

    @GetMapping("/user/bricksets")
    public ResponseEntity<List<BricksetDto>> getAllBricksetsForUser(Principal principal) {
        List<BricksetDto> bricksetDtoList = bricksetService.getAllBricksetsForUser(principal).stream()
                .map(bricksetFacade::bricksetToBricksetDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bricksetDtoList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createBrickset(@Valid @RequestBody BricksetDto bricksetDto,
                                                 BindingResult bindingResult,
                                                 Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Brickset brickset = bricksetService.createBrickset(bricksetDto, principal);

        BricksetDto bricksetCreated = bricksetFacade.bricksetToBricksetDto(brickset);
        return new ResponseEntity<>(bricksetCreated, HttpStatus.OK);
    }

    @PostMapping("/{bricksetId}/updatebrickset")
    public ResponseEntity<Object> updateBrickset(@PathVariable("bricksetId") String bricksetId,
                                                 @Valid @RequestBody BricksetDto bricksetDto,
                                                 BindingResult bindingResult,
                                                 Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Brickset brickset = bricksetService.updateBrickset(Long.parseLong(bricksetId), bricksetDto, principal);

        BricksetDto bricksetUpdated = bricksetFacade.bricksetToBricksetDto(brickset);
        return new ResponseEntity<>(bricksetUpdated, HttpStatus.OK);
    }

    @PostMapping("/{bricksetId}/{username}/like")
    public ResponseEntity<BricksetDto> likeBrickset(@PathVariable("bricksetId") String bricksetId,
                                                    @PathVariable("username") String username) {
        Brickset brickset = bricksetService.likeBrickset(Long.parseLong(bricksetId), username);
        BricksetDto bricksetDto = bricksetFacade.bricksetToBricksetDto(brickset);
        return new ResponseEntity<>(bricksetDto, HttpStatus.OK);
    }

    @PostMapping("/{bricksetId}/delete")
    public ResponseEntity<MessageResponse> deleteBrickset(@PathVariable("bricksetId") String bricksetId,
                                                          Principal principal) {
        bricksetService.deleteBrickset(Long.parseLong(bricksetId), principal);
        return new ResponseEntity<>(new MessageResponse("Brickset was deleted"), HttpStatus.OK);
    }
}
