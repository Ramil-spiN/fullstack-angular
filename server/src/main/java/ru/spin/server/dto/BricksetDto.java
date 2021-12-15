package ru.spin.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class BricksetDto {
    private Long id;
    @NotEmpty
    private String title;
    private String caption;
    private String inventory;
    private String username;
    private Integer likes;
    private Set<String> likedUsers;
}
