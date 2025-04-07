package com.planet.reservation.reservation;

import com.planet.reservation.book.Book;
import com.planet.reservation.user.User;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(
    properties = {
      "spring.datasource.url=jdbc:h2:mem:planet-test-db",
      "spring.jpa.hibernate.ddl-auto=create-drop"
    })
class ReservationRepositoryTests {

  @Autowired ReservationRepository reservationRepository;

  @Autowired TestEntityManager entityManager;

  Book book1;
  Book book2;
  Book book3;
  User user1;
  User user2;

  @BeforeEach
  void setUp() {
    book1 = Book.builder()
        .title("The Catcher in the Rye")
        .author("Salinger, J. D.")
        .isbn("isbn")
        .copies(5)
        .reservations(List.of())
        .build();

    book2 = Book.builder()
        .title("Animal Farm")
        .author("Orwell, George")
        .isbn("asd")
        .copies(10)
        .reservations(List.of())
        .build();

    book3 = Book.builder()
        .title("Animal Farm 2")
        .author("Orwell, George")
        .isbn("asd")
        .copies(10)
        .reservations(List.of())
        .build();

    user1 = User.builder()
        .name("Vinicius")
        .email("vinicius@email.com")
        .reservations(List.of())
        .build();

    user2 = User.builder()
        .name("Taynara")
        .email("taynara@email.com")
        .reservations(List.of())
        .build();


    entityManager.persist(book1);
    entityManager.persist(book2);
    entityManager.persist(user1);
    entityManager.persist(user2);

    long now = Instant.now().toEpochMilli();

    Reservation reservation1 =
        Reservation.builder()
            .id(null)
            .user(user1)
            .book(book1)
            .status(ReservationStatus.PENDING)
            .createdAt(now)
            .build();

    Reservation reservation2 =
        Reservation.builder()
            .id(null)
            .user(user2)
            .book(book2)
            .status(ReservationStatus.ACTIVE)
            .createdAt(now)
            .build();

    Reservation reservation3 =
        Reservation.builder()
            .id(null)
            .user(user1)
            .book(book2)
            .status(ReservationStatus.ACTIVE)
            .createdAt(now)
            .build();

    entityManager.persist(reservation1);
    entityManager.persist(reservation2);
    entityManager.persist(reservation3);
  }

  @AfterEach
  void tearDown() {
    entityManager.clear();
  }

  @Test
  void when_existsByUserIdAndBookId_ShouldReturnTrue() {
    boolean exists = reservationRepository.existsByUserIdAndBookId(user1.getId(), book1.getId());

    assertTrue(exists);
  }

  @Test
  void when_existsByUserIdAndBookId_ShouldReturnFalse() {
    boolean exists = reservationRepository.existsByUserIdAndBookId(user1.getId(), book3.getId());

    assertFalse(exists);
  }

  @Test
  void when_countByUserIdAndStatusIsNot_shouldFilterByStatus() {
    long actual =
        reservationRepository.countByUserIdAndStatusIsNot(user1.getId(), ReservationStatus.ACTIVE);

    assertEquals(1L, actual);
  }

  @Test
  void when_findAllByUserId_ShouldReturnAll() {
    List<Reservation> reservations1 = reservationRepository.findByUserId(user1.getId());
    List<Reservation> reservations2 = reservationRepository.findByUserId(user2.getId());

    assertEquals(2, reservations1.size());
    assertEquals(1, reservations2.size());
  }

  /**
   * 2 reservations: one created after cutoff, other one before.
   */
  @Test
  void when_findByStatusAndCreatedAtBefore_ShouldReturnOneReservation() {
    long cutoff = Instant.now().plusMillis(10000).toEpochMilli();
    long after = Instant.now().plusMillis(20000).toEpochMilli();

    Reservation r =
        Reservation.builder()
            .id(null)
            .user(user2)
            .book(book2)
            .status(ReservationStatus.PENDING)
            .createdAt(after)
            .build();
    entityManager.persist(r);

    List<Reservation> actual =
        reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, cutoff);

    assertEquals(1, actual.size());

    assertTrue(
        actual.stream()
            .allMatch(reservation -> reservation.getUser().getId().equals(user1.getId())));
  }
}
