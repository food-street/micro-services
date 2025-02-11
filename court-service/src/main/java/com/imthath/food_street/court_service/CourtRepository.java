package com.imthath.food_street.court_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {}
