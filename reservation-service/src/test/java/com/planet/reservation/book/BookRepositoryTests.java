package com.planet.reservation.book;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class BookRepositoryTests {

  @Autowired BookRepository bookRepository;

  @Autowired
  TestEntityManager entityManager;

  Book book;

  @BeforeEach
  void setUp() {
    book = Book.builder()
        .title("The Catcher in the Rye")
        .author("Salinger, J. D.")
        .isbn("isbn")
        .copies(5)
        .reservations(List.of())
        .build();

    entityManager.persist(book);
  }

  @AfterEach
  void tearDown() {
    entityManager.clear();
  }

  @Test
  void when_BookIsAvailable_shouldReturnTrue() {
    boolean actual = bookRepository.existsByIdAndCopiesGreaterThanEqual(book.getId(), 1);

    assertTrue(actual);
  }

  @Test
  void when_BookIsAvailable_shouldReturnFalse() {
    boolean actual = bookRepository.existsByIdAndCopiesGreaterThanEqual(book.getId(), 6);

    assertFalse(actual);
  }
}
