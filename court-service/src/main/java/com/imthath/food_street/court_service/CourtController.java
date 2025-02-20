package com.imthath.food_street.court_service;

import com.imthath.food_street.court_service.dto.CourtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/court")
public class CourtController {
    @Autowired
    private CourtService courtService;

    @PostMapping
    public ResponseEntity<Court> createCourt(@Valid @RequestBody CourtRequest request) {
        Court court = courtService.createCourt(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(court);
    }

    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts() {
        List<Court> courts = courtService.getAllCourts();
        return ResponseEntity.ok(courts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourtById(@PathVariable Long id) throws Exception {
        Court court = courtService.getCourtById(id);
        return ResponseEntity.ok(court);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Court> updateCourt(
            @PathVariable Long id,
            @Valid @RequestBody CourtRequest request) throws Exception {
        Court updated = courtService.updateCourt(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) throws Exception {
        courtService.deleteCourt(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkCourtExists(@RequestParam Long id) {
        boolean exists = courtService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
