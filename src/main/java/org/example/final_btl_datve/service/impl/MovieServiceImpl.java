package org.example.final_btl_datve.service.impl;

import org.example.final_btl_datve.dto.MovieDto;
import org.example.final_btl_datve.entity.Booking;
import org.example.final_btl_datve.entity.Movie;
import org.example.final_btl_datve.repository.BookingRepository;
import org.example.final_btl_datve.repository.MovieRepository;
import org.example.final_btl_datve.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, BookingRepository bookingRepository) {
        this.movieRepository = movieRepository;
        this.bookingRepository = bookingRepository;
    }

    private MovieDto convertToDto(Movie movie){
        return MovieDto.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .movieDescription(movie.getMovieDescription())
                .movieActor(movie.getMovieActor())
                .movieDirector(movie.getMovieDirector())
                .movieDuration(movie.getMovieDuration())
                .movieLanguage(movie.getMovieLanguage())
                .moviePoster(movie.getMoviePoster())
                .movieTrailer(movie.getMovieTrailer())
                .movieReleaseDate(movie.getMovieReleaseDate())
                .movie_genreList(movie.getMovie_genreList())
                .movieId(movie.getMovieId())
                .showtimeList(movie.getShowtimeList())
                .build();
    }

    @Override
    public MovieDto create(MovieDto movieDto) throws Exception{
        Movie movie = Movie.builder()
                .movieName(movieDto.getMovieName())
                .movieDescription(movieDto.getMovieDescription())
                .movieActor(movieDto.getMovieActor())
                .movieDirector(movieDto.getMovieDirector())
                .movieDuration(movieDto.getMovieDuration())
                .movieLanguage(movieDto.getMovieLanguage())
                .moviePoster(movieDto.getMoviePoster())
                .movieTrailer(movieDto.getMovieTrailer())
                .movieReleaseDate(movieDto.getMovieReleaseDate())
                .build();
        return convertToDto(movieRepository.save(movie));
    }

    @Override
    public List<MovieDto> reads() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = movies.stream().map(this::convertToDto).collect(Collectors.toList());

        List<Booking> bookings = bookingRepository.findAll();
        // Calculate views for each movie
        for (MovieDto movieDto : movieDtos) {
            Long views = bookings.stream()
                    .filter(booking -> booking.getShowtime().getMovie().getMovieId().equals(movieDto.getMovieId()))
                    .mapToLong(booking -> booking.getBooking_seats().size())
                    .sum();
            movieDto.setMovieViews(views);
        }

        return movieDtos;
    }

    @Override
    public MovieDto read(Long movieID) throws Exception {
        return movieRepository.findById(movieID)
                .map(this::convertToDto)
                .orElseThrow(() -> new Exception("Not found movie with ID: " + movieID));
    }

    @Override
    public MovieDto update(Long movieID, MovieDto movieDto) throws Exception {
        Movie existingMovie = movieRepository.findById(movieID)
                .orElseThrow(() -> new Exception("Not found movie with ID: " + movieID));
        Movie updatedMovie = existingMovie.toBuilder()
                .movieName(movieDto.getMovieName())
                .movieDescription(movieDto.getMovieDescription())
                .movieActor(movieDto.getMovieActor())
                .movieDirector(movieDto.getMovieDirector())
                .movieDuration(movieDto.getMovieDuration())
                .movieLanguage(movieDto.getMovieLanguage())
                .moviePoster(movieDto.getMoviePoster())
                .movieTrailer(movieDto.getMovieTrailer())
                .movieReleaseDate(movieDto.getMovieReleaseDate())
                .build();
        return convertToDto(movieRepository.save(updatedMovie));
    }

    @Override
    public void delete(Long movieID) {
        if (!movieRepository.existsById(movieID)) {
            throw new RuntimeException("Not found movie with ID: " + movieID);
        }
        movieRepository.deleteById(movieID);
    }

    @Override
    public List<MovieDto> search(String keyword) {
        return movieRepository.searchByKeyword(keyword).stream().map(this::convertToDto).toList();
    }


}
