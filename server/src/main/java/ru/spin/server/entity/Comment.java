package ru.spin.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String username;
    @ManyToOne
    private Brickset brickset;
    @Column(columnDefinition = "text", nullable = false)
    private String message;
    @Column(updatable = false)
    private LocalDateTime createDate;

    public Comment() {}

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
}
