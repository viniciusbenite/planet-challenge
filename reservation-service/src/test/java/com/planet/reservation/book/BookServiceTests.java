package com.planet.reservation.book;

import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
  @Mock
  BookRepository bookRepository;

  @Mock
  BookMapper bookMapper;

  @InjectMocks
  BookService bookService;

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
  }

  @Test
  void when_updateBookInventory_shouldAddCopy() {
    Book updateBook = Book.builder()
        .title("The Catcher in the Rye")
        .author("Salinger, J. D.")
        .isbn("isbn")
        .copies(6)
        .reservations(List.of())
        .build();

    bookService.updateBookInventory(book, BookAction.ADD);

    verify(bookRepository).save(updateBook);
  }

  @Test
  void when_updateBookInventory_shouldRemoveCopy() {
    Book updateBook = Book.builder()
        .title("The Catcher in the Rye")
        .author("Salinger, J. D.")
        .isbn("isbn")
        .copies(4)
        .reservations(List.of())
        .build();

    bookService.updateBookInventory(book, BookAction.REMOVE);

    verify(bookRepository).save(updateBook);
  }
}
