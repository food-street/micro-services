package com.imthath.food_street.restaurant_service;

import com.imthath.food_street.restaurant_service.dto.RestaurantRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant createRestaurant(@Valid @RequestBody RestaurantRequest request) throws Exception {
        return restaurantService.createRestaurant(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Restaurant> getRestaurants(@RequestParam(required = false) Long courtId) throws Exception {
        if (courtId != null) {
            return restaurantService.getRestaurantsByCourtId(courtId);
        }
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Restaurant getRestaurantById(@PathVariable Long id) throws Exception {
        return restaurantService.getRestaurantById(id);
    }

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkRestaurantExists(@RequestParam long id) {
        return restaurantService.existsById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Restaurant updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantRequest request) throws Exception {
        return restaurantService.updateRestaurant(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable Long id) throws Exception {
        restaurantService.deleteRestaurant(id);
    }
}