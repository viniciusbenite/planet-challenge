package com.planet.reservation.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
  private Long userId;
  @NotEmpty private String name;
  @NotEmpty private String email;
}
