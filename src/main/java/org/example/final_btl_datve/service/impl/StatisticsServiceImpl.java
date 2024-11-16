package org.example.final_btl_datve.service.impl;

import org.example.final_btl_datve.dto.MovieViewData;
import org.example.final_btl_datve.entity.Booking;
import org.example.final_btl_datve.entity.Movie;
import org.example.final_btl_datve.repository.BookingRepository;
import org.example.final_btl_datve.repository.MovieRepository;
import org.example.final_btl_datve.repository.UserRepository;
import org.example.final_btl_datve.service.StatisticsService;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatisticsServiceImpl implements StatisticsService {
    private final MovieRepository movieRepository;

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    public StatisticsServiceImpl(MovieRepository movieRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long getTotalTicketSold() {
        List<Booking> bookings = bookingRepository.findAll();
        Long total= 0L;
        for (Booking booking : bookings) {
            total += booking.getBooking_seats().size();
        }
        return total;
    }

    @Override
    public Double getTotalRevenue() {
        List<Booking> bookings = bookingRepository.findAll();
        Double total = bookings.stream().mapToDouble(Booking::getTotalPrice).sum();
        return Math.round(total*0.00003945*100.0)*100.0;
    }

    @Override
    public Long getTotalUser() {
        return userRepository.count();
    }

    @Override
    public Long getTotalMovie() {
        return movieRepository.count();
    }

    @Override
    public List<MovieViewData> getTopMovie() {
        // Lấy tất cả các booking
        List<Booking> bookings = bookingRepository.findAll();

        // Tạo một Map để nhóm theo Movie và tính toán views
        Map<Movie, Long> movieViewsMap = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getShowtime().getMovie(), // Lấy Movie từ Showtime
                        Collectors.summingLong(booking -> booking.getBooking_seats().size()) // Tính tổng số ghế cho mỗi phim
                ));

        // Chuyển Map thành một danh sách MovieViewData chứa thông tin về tên, thể loại, thời gian và views
        List<MovieViewData> movieViewDataList = movieViewsMap.entrySet().stream()
                .map(entry -> {
                    Movie movie = entry.getKey();
                    Long views = entry.getValue();

                    // Tạo MovieViewData cho mỗi phim với thông tin tên, thể loại, thời gian và lượt xem
                    MovieViewData movieViewData = new MovieViewData();
                    movieViewData.setName(movie.getMovieName());
                    movieViewData.setPoster(movie.getMoviePoster());
                    movieViewData.setDuration(movie.getMovieDuration());
                    movieViewData.setViews(views);

                    // Lấy thể loại từ movie_genre
                    List<String> genres = movie.getMovie_genreList().stream()
                            .map(movieGenre -> movieGenre.getGenre().getGenreName()) // Lấy tên thể loại
                            .collect(Collectors.toList());

                    movieViewData.setGenres(genres);
                    return movieViewData;
                })
                .sorted(Comparator.comparingLong(MovieViewData::getViews).reversed()) // Sắp xếp theo views từ cao đến thấp
                .limit(5) // Lấy 5 phim hot nhất
                .collect(Collectors.toList());
        return movieViewDataList;
    }
}
