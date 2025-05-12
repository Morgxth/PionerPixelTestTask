package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_data")
@Data
@NoArgsConstructor @AllArgsConstructor
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
