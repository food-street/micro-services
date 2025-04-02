package com.imthath.food_street.court_service;

import com.imthath.food_street.court_service.dto.CourtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/court")
public class CourtController {
    @Autowired
    private CourtService courtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Court createCourt(@Valid @RequestBody CourtRequest request) {
        return courtService.createCourt(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Court getCourtById(@PathVariable Long id) throws Exception {
        return courtService.getCourtById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Court updateCourt(
            @PathVariable Long id,
            @Valid @RequestBody CourtRequest request) throws Exception {
        return courtService.updateCourt(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourt(@PathVariable Long id) throws Exception {
        courtService.deleteCourt(id);
    }

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkCourtExists(@RequestParam Long id) {
        return courtService.existsById(id);
    }
}
