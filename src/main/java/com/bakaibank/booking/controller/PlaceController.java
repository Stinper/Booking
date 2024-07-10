package com.bakaibank.booking.controller;

import com.bakaibank.booking.dto.place.CreatePlaceDTO;
import com.bakaibank.booking.dto.place.PlaceDTO;
import com.bakaibank.booking.dto.place.PlaceWithBookingDTO;
import com.bakaibank.booking.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<PlaceDTO> findAllPlaces(@RequestParam(value = "date", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        LocalDate date) {
        return placeService.findAll(date != null ? date : LocalDate.now());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceWithBookingDTO> findPlaceById(@PathVariable Long id,
                                                             @RequestParam(value = "date", required = false)
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                             LocalDate date) {
        return placeService.findByIdAndDateWithBookingInfo(id, date != null ? date : LocalDate.now())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceDTO createPlace(@RequestBody CreatePlaceDTO createPlaceDTO) {
        return placeService.save(createPlaceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaceById(@PathVariable Long id) {
        return placeService.findById(id)
                .map(place -> {
                    placeService.deleteById(id);
                    return ResponseEntity.ok(place);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
