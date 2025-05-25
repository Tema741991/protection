package ru.top.cinemas.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "Seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`row_number`")
    private String rowNumber;

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name="active_seat")
    private boolean activeSeat=true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id",nullable = false)
    private Hall hall;

}
