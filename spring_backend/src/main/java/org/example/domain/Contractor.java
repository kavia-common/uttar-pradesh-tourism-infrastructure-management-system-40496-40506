package org.example.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contractors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contractor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
