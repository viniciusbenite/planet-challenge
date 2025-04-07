package com.planet.reservation.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management related endpoints")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(
        summary = "Create book",
        description = "Create and returns the created book details"
    )
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO book = bookService.addBook(bookDTO);

        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/{bookId}")
    @Operation(
        summary = "Get book by ID",
        description = "Returns details of a book"
    )
    public ResponseEntity<BookDTO> getBook(@PathVariable Long bookId) {
        BookDTO book = bookService.getBook(bookId);

        return ResponseEntity.ok().body(book);
    }

    // Other CRUD endpoints ...
}
