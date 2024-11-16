package org.example.final_btl_datve.service;

import org.example.final_btl_datve.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //CRUD
    List<UserDto> reads();
    UserDto read(Long userID) throws Exception;
    UserDto update(Long userID, UserDto userDto) throws Exception;

    //admin
    UserDto create(UserDto userDto) throws Exception;
    void delete(Long userID) throws Exception;

    //Other
    //List<BookingRequest> getBookingHistory(Long userID) throws Exception;
}
