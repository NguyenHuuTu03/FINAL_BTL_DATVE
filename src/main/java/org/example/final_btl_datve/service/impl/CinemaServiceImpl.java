package org.example.final_btl_datve.service.impl;

import org.example.final_btl_datve.dto.CinemaDto;
import org.example.final_btl_datve.dto.MovieDto;
import org.example.final_btl_datve.dto.ScreeningRoomDto;
import org.example.final_btl_datve.entity.Cinema;
import org.example.final_btl_datve.repository.CinemaRepository;
import org.example.final_btl_datve.repository.LocationRepository;
import org.example.final_btl_datve.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CinemaServiceImpl implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public CinemaServiceImpl(CinemaRepository cinemaRepository, LocationRepository locationRepository) {
        this.cinemaRepository = cinemaRepository;
        this.locationRepository = locationRepository;
    }

    private CinemaDto convertToDto(Cinema cinema) {
        return CinemaDto.builder()
                .cinemaId(cinema.getCinemaId())
                .cinemaName(cinema.getCinemaName())
                .detailedAddress(cinema.getDetailedAddress())
                .hotline(cinema.getHotline())
                .locationId(cinema.getLocation().getLocationId())
                .build();
    }

    @Override
    public CinemaDto create(CinemaDto cinemaDto) throws Exception {
        Cinema cinema = Cinema.builder()
                .cinemaName(cinemaDto.getCinemaName())
                .detailedAddress(cinemaDto.getDetailedAddress())
                .hotline(cinemaDto.getHotline())
                .location(locationRepository.findById(cinemaDto.getCinemaId())
                        .orElseThrow(() -> {
                            return new Exception("Không tìm thấy địa điểm có ID: " + cinemaDto.getLocationId());
                        }))
                .build();
        return convertToDto(cinemaRepository.save(cinema));
    }

    @Override
    public List<CinemaDto> reads() {
        return cinemaRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Override
    public CinemaDto read(Long cinemaID) throws Exception {
        return cinemaRepository.findById(cinemaID)
                .map(this::convertToDto)
                .orElseThrow(() -> {
                    return new Exception("Không tìm thấy rạp có ID: " + cinemaID);
                });
    }

    @Override
    public CinemaDto update(Long cinemaID, CinemaDto cinemaDto) throws Exception {
        Cinema cinema = cinemaRepository.findById(cinemaID)
                .orElseThrow(() -> {
                    return new Exception("Không tìm thấy rạp có ID: " + cinemaID);
                });
        cinema.toBuilder()
                .cinemaName(cinemaDto.getCinemaName())
                .detailedAddress(cinemaDto.getDetailedAddress())
                .hotline(cinemaDto.getHotline())
                .location(locationRepository.findById(cinemaDto.getCinemaId())
                        .orElseThrow(() -> {
                            return new Exception("Không tìm thấy địa điểm : " + cinemaDto.getCinemaName());
                        }))
                .build();
        return convertToDto(cinemaRepository.save(cinema));
    }

    @Override
    public void delete(Long cinemaID) throws Exception {
        if (!cinemaRepository.existsById(cinemaID)) {
            throw new Exception("Không tìm thấy rạp có ID: " + cinemaID);
        }
        cinemaRepository.deleteById(cinemaID);
    }

    @Override
    public List<ScreeningRoomDto> getRoomByCinema(Long cinemaID) {
        return cinemaRepository.findById(cinemaID)
                .map(cinema -> cinema.getScreeningRoomList().stream().map(room -> {
                    ScreeningRoomDto roomDto = new ScreeningRoomDto();
                    roomDto.setRoomName(room.getRoomName());
                    return roomDto;
                }).toList())
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<MovieDto> getMovieByCinema(Long cinemaID) {
        return cinemaRepository.findById(cinemaID)
                .map(cinema -> cinema.getScreeningRoomList().stream()
                        .flatMap(screeningRoom -> screeningRoom.getShowtimeList().stream()) // Lấy danh sách showtime từ mỗi phòng chiếu
                        .map(showtime -> showtime.getMovie()) // Lấy phim từ showtime
                        .distinct() // Để loại bỏ phim trùng lặp
                        .map(movie -> {
                            MovieDto movieDto = new MovieDto();
                            movieDto.setMovieName(movie.getMovieName());
                            return movieDto;
                        }).toList())
                .orElseGet(Collections::emptyList);
    }
}
