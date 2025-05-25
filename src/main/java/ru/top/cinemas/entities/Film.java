package ru.top.cinemas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "Films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "title", nullable = false)
    private String title;


    @Column(name = "year", nullable = false)
    private int year;


    @Column(name = "description", nullable = false,length = 1000)
    private String description;


    @Column(name = "rating",precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "poster_path")
    private String posterPath;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "film_genres",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "film")
    @JsonIgnore
    private List<Session> session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ageRating_id",nullable = false)
    private AgeRating ageRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FilmStatus status = FilmStatus.SOON;

}
