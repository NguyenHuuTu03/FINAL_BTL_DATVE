package org.example.final_btl_datve.controller;

import org.example.final_btl_datve.dto.ShowtimeDto;
import org.example.final_btl_datve.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ShowtimeDto showtimeDto) throws Exception{
        return ResponseEntity.ok().body(showtimeService.create(showtimeDto));
    }

    @GetMapping("/all")
    public ResponseEntity<?> reads(){
        return ResponseEntity.ok().body(showtimeService.reads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable long id) throws Exception{
        return ResponseEntity.ok().body(showtimeService.read(id));
    }

    //Xem suất chiếu theo id phim
    @GetMapping("/movie/{id}")
    public ResponseEntity<?> getShowtimeByMovieId(@PathVariable long id){
        return ResponseEntity.ok().body(showtimeService.getShowtimeByMovieId(id));
    }

    //Xem ghế trống theo id suất chiếu
    @GetMapping("/empty-seat/{id}")
    public ResponseEntity<?> getEmptySeatByShowtimeId(@PathVariable long id){
        return ResponseEntity.ok().body(showtimeService.getEmptySeatByShowtimeId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @RequestBody ShowtimeDto showtimeDto) throws Exception{
        return ResponseEntity.ok().body(showtimeService.update(id, showtimeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) throws Exception{
        showtimeService.delete(id);
        return ResponseEntity.ok("Xóa suất chiếu thành công");
    }
}