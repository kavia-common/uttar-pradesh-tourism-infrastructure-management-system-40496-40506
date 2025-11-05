package org.example.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "progress")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Progress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;

    private String mediaRef;

    @ManyToOne
    private Project project;
}
