package org.example.final_btl_datve.controller;

import org.example.final_btl_datve.dto.MovieDto;
import org.example.final_btl_datve.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<?> reads(){
        return ResponseEntity.ok().body(movieService.reads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) throws Exception{
        return ResponseEntity.ok().body(movieService.read(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MovieDto movieDto) throws Exception{
        return ResponseEntity.ok().body(movieService.create(movieDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody MovieDto movieDto) throws Exception{
        return ResponseEntity.ok().body(movieService.update(id, movieDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception{
        movieService.delete(id);
        return ResponseEntity.ok("Xóa phim thành công");
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword){
        return ResponseEntity.ok().body(movieService.search(keyword));
    }
}

