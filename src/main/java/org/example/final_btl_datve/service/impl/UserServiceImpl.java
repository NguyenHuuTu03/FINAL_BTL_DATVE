package org.example.final_btl_datve.service.impl;

import org.example.final_btl_datve.dto.UserDto;
import org.example.final_btl_datve.entity.User;
import org.example.final_btl_datve.entity.enumModel.ERole;
import org.example.final_btl_datve.repository.UserRepository;
import org.example.final_btl_datve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .address(user.getAddress())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .build();
    }

    //
    @Override
    public UserDto create (UserDto userDto) throws Exception {
        if(userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new Exception("Email đã tồn tại");
        }
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .gender(userDto.getGender())
                .birthday(userDto.getBirthday())
                .build();
        return convertToDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> reads () {
        return userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public UserDto read (Long userID) throws Exception {
        return userRepository.findById(userID).map(this::convertToDto).orElseThrow(() -> {
            return new Exception("Người dùng không tồn tại");
        });
    }

    @Override
    public UserDto update (Long userID, UserDto userDto) throws Exception {
        User existingUser = userRepository.findById(userID).orElseThrow(() -> {
            return new Exception("Người dùng không tồn tại");
        });

        User updatedUser = existingUser.toBuilder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .birthday(userDto.getBirthday())
                .build();
        return convertToDto(userRepository.save(updatedUser));
    }

    @Override
    public void delete (Long userID) throws Exception {
        if(!userRepository.existsById(userID)) {
            throw new Exception("Người dùng không tồn tại");
        }
        userRepository.deleteById(userID);
    }

//    @Override
//    public List<BookingRequest> getBookingHistory(Long userID) throws Exception {
//        User user = userRepository.findById(userID).orElseThrow(() -> {
//            return new Exception("Người dùng không tồn tại");
//        });
//
//        return user.getBookings().stream().map(booking -> BookingRequest.builder()
//                        .showtime(convertToDto(booking.getShowtime()))
//                        .seats(booking.getBooking_seats().stream()
//                                .map(Booking_Seat::getSeat)
//                                .collect(Collectors.toList()))
//                        .build())
//                .collect(Collectors.toList());
//
//    }

//    private Showtime convertToDto(Showtime showtime) {
//        return Showtime.builder()
//                .showtimeID(showtime.getShowtimeID())
//                .movie(showtime.getMovie())
//                .room(showtime.getRoom())
//                .startTime(showtime.getStartTime())
//                .build();
//    }
//
//    private SeatDto convertToDto(Seat seat) {
//        return SeatDto.builder()
//                .seatNumber(seat.getSeatNumber())
//                .seatRow(seat.getSeatRow())
//                .build();
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}

