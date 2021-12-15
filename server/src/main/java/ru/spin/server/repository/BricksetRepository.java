package ru.spin.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.spin.server.entity.Brickset;
import ru.spin.server.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BricksetRepository extends JpaRepository<Brickset, Long> {
    List<Brickset> findAllByOrderByCreateDateDesc();

    List<Brickset> findAllByUserOrderByCreateDateDesc(User user);

    Optional<Brickset> findBricksetByIdAndUser(Long id, User user);
}
