package ru.top.cinemas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "age_ratings")

public class AgeRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, unique = true)
    private String code; //  0+, 6+, 12+, 16+, 18+

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name; // "Для всех возрастов", "Детям до 16 с родителями" и т.д.

    @Size(max = 500)
    private String description;

    @Min(0)
    @Max(21)
    @Column(nullable = false)
    private int minAge; // Минимальный рекомендуемый возраст

    @OneToMany(mappedBy = "ageRating")
    private List<Film> films;

}