package com.planet.reservation.book;

import com.planet.reservation.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDTO addBook(BookDTO bookDTO) {
        Book book = bookMapper.toModel(bookDTO);

        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Cacheable(value = "books", key = "#bookId")
    public BookDTO getBook(Long bookId) {
        return bookMapper.toDTO(findBook(bookId));
    }

    public Book findBook(Long bookId) {
        return bookRepository
                .findById(bookId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book %s not found".formatted(bookId)));
    }

    public boolean isBookAvailable(Long bookId) {
        return bookRepository.existsByIdAndCopiesGreaterThanEqual(bookId, 1);
    }

    @Transactional
    @CacheEvict(value = "books", key = "#book.id")
    public BookDTO updateBookInventory(Book book, BookAction action) {
        int bookCopies = book.getCopies();

        if (action == BookAction.ADD) {
            book.setCopies(bookCopies + 1);
        }
        if (action == BookAction.REMOVE) {
            book.setCopies(bookCopies - 1);
        }

      return bookMapper.toDTO(bookRepository.save(book));
    }
}
