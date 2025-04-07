package com.planet.reservation.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  boolean existsByUserIdAndBookId(Long userId, Long bookId);

  long countByUserIdAndStatusIsNot(Long userId, ReservationStatus status);

  List<Reservation> findByUserId(Long userId);

  List<Reservation> findByStatusAndCreatedAtBefore(ReservationStatus status, Long createdAt);
}
