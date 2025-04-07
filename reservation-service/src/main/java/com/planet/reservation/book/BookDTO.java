package com.planet.reservation.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDTO {
  private Long bookId;
  @NotEmpty private String title;
  @NotEmpty private String author;
  @NotEmpty private String isbn;
  @NotEmpty @PositiveOrZero private Integer copies;
}
