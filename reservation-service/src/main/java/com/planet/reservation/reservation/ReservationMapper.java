package com.planet.reservation.reservation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "id", target = "reservationId")
    ReservationDTO toDTO(Reservation reservation);

    Reservation toModel(ReservationDTO reservationDTO);
}
