package org.example.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "handovers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Handover {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String notes;

    @ManyToOne
    private Project project;
}
