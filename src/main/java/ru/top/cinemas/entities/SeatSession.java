package ru.top.cinemas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seat_session")

public class SeatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id",nullable = false)
    Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seatTemplate_id",nullable = false)
    Seat seatTemplate;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private BigDecimal price= BigDecimal.valueOf(190.00);

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.ACTIVE;

     @Transient
    public String getStatusClass() {
        return status.name().toLowerCase();
    }
}
