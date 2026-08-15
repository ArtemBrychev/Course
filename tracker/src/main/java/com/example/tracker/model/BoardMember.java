package com.example.tracker.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "board_members")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}