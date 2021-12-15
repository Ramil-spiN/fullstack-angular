package ru.spin.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spin.server.entity.ImageModel;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findImageModelByUserId(Long userId);

    Optional<ImageModel> findImageModelByBricksetId(Long bricksetId);
}
