package com.imthath.food_street.court_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourtService {
    @Autowired
    CourtRepository courtRepository;

    Optional<Court> getCourt(Long id) {
        return courtRepository.findById(id);
    }
}
