package com.planet.reservation.user;

import com.planet.reservation.reservation.Reservation;
import com.planet.reservation.util.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management related endpoints")
public class UserController {
  private final UserService userService;

  @PostMapping
  @Operation(
      summary = "Create user",
      description = "Create and returns the created user details"
  )
  public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
    UserDTO user = userService.addUser(userDTO);

    return ResponseEntity.ok().body(user);
  }

  @GetMapping("/{userId}/reservations")
  @Operation(
      summary = "Get all reservations by user ID",
      description = "Returns list of reservations by user"
  )
  public ResponseEntity<ListResponse> reservationHistory(
      @PathVariable
          @NotNull(message = "User ID must not be null")
          @Positive(message = "User ID has to be bigger than 0")
          Long userId) {

    List<Reservation> reservations = userService.listUserReservations(userId);
    ListResponse response =
        ListResponse.builder().total(reservations.size()).data(reservations).build();

    return ResponseEntity.ok(response);
  }

  // Other CRUD endpoints ...
}
