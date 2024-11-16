package org.example.final_btl_datve.controller;

import org.example.final_btl_datve.dto.LocationDto;
import org.example.final_btl_datve.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> reads(){
        return ResponseEntity.ok().body(locationService.reads());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> read(@PathVariable Long id) throws Exception{
        return ResponseEntity.ok().body(locationService.read(id));
    }

    // Xem phim theo địa điểm
    @GetMapping("/{locationId}/cinemas")
    public ResponseEntity<?> getCinemaByLocation(@PathVariable Long locationId){
        return ResponseEntity.ok().body(locationService.getCinemaByLocation(locationId));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody LocationDto locationDto) throws Exception{
        return ResponseEntity.ok().body(locationService.update(id, locationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws Exception{
        locationService.delete(id);
        return ResponseEntity.ok("Xóa địa điểm thành công");
    }
}
