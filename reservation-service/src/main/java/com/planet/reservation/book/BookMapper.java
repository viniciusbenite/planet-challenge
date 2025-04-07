package com.planet.reservation.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "id", target = "bookId")
    BookDTO toDTO(Book book);

    Book toModel(BookDTO bookDTO);
}
