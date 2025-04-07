package com.planet.reservation.reservation;

import com.planet.reservation.book.Book;
import com.planet.reservation.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationDTO {
    private Long reservationId;
    private User user;
    private Book book;
    private ReservationStatus status;
    private Long createdAt;
}
