package com.imthath.food_street.restaurant_service;

import com.imthath.food_street.restaurant_service.dto.RestaurantRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody RestaurantRequest request) throws Exception {
        Restaurant restaurant = restaurantService.createRestaurant(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurant);
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurants(@RequestParam(required = false) Long courtId) throws Exception {
        List<Restaurant> restaurants;
        if (courtId != null) {
            restaurants = restaurantService.getRestaurantsByCourtId(courtId);
        } else {
            restaurants = restaurantService.getAllRestaurants();
        }
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) throws Exception {
        Restaurant updated = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) throws Exception {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}