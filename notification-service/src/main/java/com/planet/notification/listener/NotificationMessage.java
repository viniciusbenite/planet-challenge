package com.planet.notification.listener;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {
    private String email;
    private String userName;
    private String bookTitle;
    private String isbn;
    private ReservationStatus reservationStatus;
}
