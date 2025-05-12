package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "phone_data")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 13, nullable = false, unique = true)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

