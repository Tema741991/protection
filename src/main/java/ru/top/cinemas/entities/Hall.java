package ru.top.cinemas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "Halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",unique = true)
    private String name;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "number_rows")
    private int numberOfRows;

    @Column(name = "seats_rows")
    private int numberSeatsOfRows;

    @Column(name = "deactive_places")
    private int deactivePlaces;

    @Column(name = "active_hall")
    private boolean activeHall;


    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Session> sessionList;


    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL,  fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Seat> seatList = new ArrayList<>();

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechnicalBreak> technicalBreaks = new ArrayList<>();

    @OneToMany(mappedBy = "hall",cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<WorkTime> workTimes;
}
