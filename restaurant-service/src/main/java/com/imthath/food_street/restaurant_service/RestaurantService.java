package com.imthath.food_street.restaurant_service;

import com.imthath.food_street.restaurant_service.dto.RestaurantRequest;
import com.imthath.food_street.restaurant_service.error.RestaurantError;
import com.imthath.utils.guardrail.GenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestTemplate restTemplate;

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
        if (request.courtId() != null) {
            verifyCourtExists(request.courtId());
        }

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setImageUrl(request.imageUrl());
        restaurant.setCourtId(request.courtId());
        restaurant.setUpdatedAt(Instant.now());
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Long id) throws Exception {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    private void verifyCourtExists(Long courtId) {
        try {
            restTemplate.getForObject("http://localhost:8083/court/" + courtId, Object.class);
        } catch (Exception e) {
            throw new GenericException(RestaurantError.COURT_NOT_FOUND);
        }
    }
} 