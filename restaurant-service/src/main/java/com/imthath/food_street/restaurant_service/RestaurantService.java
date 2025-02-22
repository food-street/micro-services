package com.imthath.food_street.restaurant_service;

import com.imthath.food_street.restaurant_service.dto.RestaurantRequest;
import com.imthath.food_street.restaurant_service.error.RestaurantError;
import com.imthath.utils.guardrail.GenericException;
import com.imthath.food_street.restaurant_service.client.CourtClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CourtClient courtClient;

    public Restaurant createRestaurant(RestaurantRequest request) throws Exception {
        // Verify if court exists if courtId is provided
        if (request.courtId() != null) {
            verifyCourtExists(request.courtId());
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setImageUrl(request.imageUrl());
        restaurant.setCourtId(request.courtId());
        restaurant.setCreatedAt(Instant.now());
        restaurant.setUpdatedAt(Instant.now());
        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getRestaurantsByCourtId(Long courtId) throws Exception {
        verifyCourtExists(courtId);
        return restaurantRepository.findByCourtId(courtId);
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new GenericException(RestaurantError.RESTAURANT_NOT_FOUND));
    }

    public Restaurant updateRestaurant(Long id, RestaurantRequest request) throws Exception {
        Restaurant restaurant = getRestaurantById(id);
        // Court ID should not be updated after creation
        if ((restaurant.getCourtId() != null && !restaurant.getCourtId().equals(request.courtId())) || 
            (restaurant.getCourtId() == null && request.courtId() != null)) {
            throw new GenericException(RestaurantError.COURT_ID_MISMATCH);
        }

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setImageUrl(request.imageUrl());
        restaurant.setUpdatedAt(Instant.now());
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Long id) throws Exception {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    private void verifyCourtExists(Long courtId) {
        boolean exists = false;
        try {
            exists = courtClient.checkCourtExists(courtId);
        } catch (Exception e) {
            log.error("Failed to check court with ID {}", courtId, e);
        }
        if (!exists) {
            log.warn("Court with ID {} not found", courtId);
            throw new GenericException(RestaurantError.COURT_NOT_FOUND);
        }
    }

    public boolean existsById(Long id) {
        return restaurantRepository.existsById(id);
    }
} 