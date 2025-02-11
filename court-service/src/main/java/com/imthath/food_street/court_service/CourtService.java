package com.imthath.food_street.court_service;

import com.imthath.food_street.court_service.dto.CourtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourtService {
    @Autowired
    CourtRepository courtRepository;

    Optional<Court> getCourt(Long id) {
        return courtRepository.findById(id);
    }

    public Court createCourt(CourtRequest request) {
        Court court = new Court();
        court.setName(request.name());
        court.setLocation(request.location());
        court.setImageUrl(request.imageUrl());
//        court.setCreatedAt(Instant.now());
//        court.setUpdatedAt(Instant.now());
        return courtRepository.save(court);
    }

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Court getCourtById(Long id) throws Exception{
        return courtRepository.findById(id)
                .orElseThrow(() -> new Exception("Court not found with id: " + id));
    }

    public Court updateCourt(Long id, CourtRequest request) throws Exception {
        Court court = getCourtById(id);
        court.setName(request.name());
        court.setLocation(request.location());
        court.setImageUrl(request.imageUrl());
//        court.setUpdatedAt(Instant.now());
        return courtRepository.save(court);
    }

    public void deleteCourt(Long id) throws Exception {
        Court court = getCourtById(id);
        courtRepository.delete(court);
    }

}
