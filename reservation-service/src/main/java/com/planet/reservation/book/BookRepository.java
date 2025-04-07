package com.planet.reservation.book;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
  boolean existsByIdAndCopiesGreaterThanEqual(Long bookId, int copies);
}
