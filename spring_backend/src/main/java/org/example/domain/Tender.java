package org.example.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tender {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne
    private Project project;
}
