package com.planet.reservation.user;

import com.planet.reservation.exception.EntityNotFoundException;
import com.planet.reservation.reservation.Reservation;
import com.planet.reservation.reservation.ReservationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReservationRepository reservationRepository;

    public UserDTO addUser(UserDTO userDTO) {
        User user = userMapper.toModel(userDTO);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Cacheable(value = "users", key = "#userId")
    public User findUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException("User %s not found".formatted(userId)));
    }

    @Cacheable(value = "user-reservations", key = "#userId")
    public List<Reservation> listUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}
