package org.example.final_btl_datve.service.impl;

import org.example.final_btl_datve.dto.BookingRequest;
import org.example.final_btl_datve.dto.BookingResponse;
import org.example.final_btl_datve.entity.*;
import org.example.final_btl_datve.entity.enumModel.PromotionType;
import org.example.final_btl_datve.entity.key.BookingComboKey;
import org.example.final_btl_datve.entity.key.BookingSeatKey;
import org.example.final_btl_datve.repository.*;
import org.example.final_btl_datve.service.BookingService;
import org.example.final_btl_datve.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ComboRepository comboRepository;
    private final PromotionRepository promotionRepository;
    private final EmailService emailService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ShowtimeRepository showtimeRepository, SeatRepository seatRepository, ComboRepository comboRepository, PromotionRepository promotionRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
        this.comboRepository = comboRepository;
        this.promotionRepository = promotionRepository;
        this.emailService = emailService;
    }

    private Booking createBookingFromRequest(BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setBookingTime(LocalDateTime.now());

        // Set user
        User user = userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        booking.setUser(user);

        // Set showtime
        Showtime showtime = showtimeRepository.findById(bookingRequest.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        booking.setShowtime(showtime);

        // Set booking_seats
        List<Booking_Seat> booking_seats = bookingRequest.getSeatIds().stream()
                .map(seatId -> {
                    Booking_Seat booking_seat = new Booking_Seat();
                    BookingSeatKey bookingSeatKey = new BookingSeatKey(booking.getBookingId(), seatId);
                    booking_seat.setId(bookingSeatKey);
                    booking_seat.setBooking(booking);
                    Seat seat = seatRepository.findById(seatId)
                            .orElseThrow(() -> new RuntimeException("Seat not found"));
                    booking_seat.setSeat(seat);
                    return booking_seat;
                })
                .collect(Collectors.toList());
        booking.setBooking_seats(booking_seats);

        // Set booking_combos
        List<Booking_Combo> booking_combos = bookingRequest.getComboIds().stream()
                .map(comboId -> {
                    Booking_Combo booking_combo = new Booking_Combo();
                    BookingComboKey key = new BookingComboKey(booking.getBookingId(), comboId);
                    booking_combo.setBookingComboKey(key);
                    booking_combo.setBooking(booking);
                    Combo combo = comboRepository.findById(comboId)
                            .orElseThrow(() -> new RuntimeException("Combo not found"));
                    booking_combo.setCombo(combo);
                    return booking_combo;
                })
                .collect(Collectors.toList());
        booking.setBooking_combos(booking_combos);

        // Set total price
        Double totalPrice = 0.0;

        List<Promotion> promotions = promotionRepository.findPromotionByTypes(List.of( PromotionType.MOVIE.name(), PromotionType.CINEMA.name(), PromotionType.COMBO.name()));
        if(promotions.isEmpty()){
            totalPrice = booking_seats.stream()
                    .map(booking_seat -> booking_seat.getSeat().getSeatType().getSeatPrice())
                    .reduce(0.0, Double::sum);
        }else {
            for (Promotion p : promotions) {
                if (p.getPromotionType().equals(PromotionType.MOVIE)) {
                    if (promotionRepository.findPromotionsByKeywordInDescription(showtime.getMovie().getMovieName())) {
                        totalPrice += p.getPrice();
                    }
                }
                if (p.getPromotionType().equals(PromotionType.CINEMA)) {
                    if (promotionRepository.findPromotionsByKeywordInDescription(showtime.getRoom().getCinema().getCinemaName())) {
                        totalPrice += p.getPrice() * booking_seats.size();
                    } else {
                        totalPrice = booking_seats.stream()
                                .map(booking_seat -> booking_seat.getSeat().getSeatType().getSeatPrice())
                                .reduce(0.0, Double::sum);
                    }
                    if (p.getPromotionType().equals(PromotionType.COMBO)) {
                        if (promotionRepository.findPromotionsByKeywordInDescription(showtime.getMovie().getMovieName())) {
                            totalPrice += p.getPrice();
                        }
                    }
                }
            }
        }

        totalPrice -= user.getAccumulatedPoints();
        if (totalPrice < 0) {
            user.setAccumulatedPoints(-totalPrice);
            totalPrice = 0.0;
        }

        // Set totalPrice
        booking.setTotalPrice(totalPrice);

        // Set points earned
        booking.calculatePoints();
        user.setAccumulatedPoints(user.getAccumulatedPoints() + booking.getPointsEarned());
        userRepository.save(user);

        return booking;
    }

    private Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    private BookingResponse createResponseFromBooking(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .userId(booking.getUser().getUserId())
                .showtimeId(booking.getShowtime().getShowtimeId())
                .totalPrice(booking.getTotalPrice())
                .pointsEarned(booking.getPointsEarned())
                .bookingTime(booking.getBookingTime())
                .seatIds(booking.getBooking_seats().stream()
                        .map(booking_seat -> booking_seat.getSeat().getSeatId())
                        .collect(Collectors.toList()))
                .comboIds(booking.getBooking_combos().stream()
                        .map(booking_combo -> booking_combo.getCombo().getComboId())
                        .collect(Collectors.toList()))
                .message("Booking successful")
                .build();
    }

    // Phương thức gửi vé điện tử qua email
    private void sendBookingConfirmation(User user, Booking booking) {
        String recipientEmail = user.getEmail();
        String subject = "Your E-Ticket Confirmation";
        String body = generateTicketDetails(booking);

        emailService.sendEmail(user.getEmail(), recipientEmail, subject, body);
    }

    // Phương thức tạo nội dung vé điện tử
    private String generateTicketDetails(Booking booking) {
        // Ví dụ về cách tạo thông tin vé điện tử
        return "Booking Details:\n" +
                "Movie: " + booking.getShowtime().getMovie().getMovieName() + "\n" +
                "Cinema: " + booking.getShowtime().getRoom().getCinema().getCinemaName() + "\n" +
                "Date: " + booking.getShowtime().getStartTime() + "\n" +
                "Price: " + booking.getTotalPrice() + "\n" +
                "Combo: " + booking.getBooking_combos().stream().map(Booking_Combo::getCombo).map(Combo::getComboName).collect(Collectors.joining(", ")) + "\n" +
                "Seats: " + booking.getBooking_seats().stream().map(Booking_Seat::getSeat).map(Seat::getSeatRow).collect(Collectors.joining(", ")) + "\n" +
                "Thank you for choosing our service!";
    }

    @Override
    public BookingResponse bookTicket(BookingRequest bookingRequest){
        Booking booking = createBookingFromRequest(bookingRequest);
        booking = saveBooking(booking);

        return createResponseFromBooking(booking);
    }
}
