package ru.top.cinemas.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "technical_break")
    public class TechnicalBreak {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "date")
        private LocalDate date;

        @Column(name = "start")
        private LocalTime start;

        @Column(name = "`end`")
        private LocalTime end;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "hall_id",nullable = false)
        private Hall hall;

    }
