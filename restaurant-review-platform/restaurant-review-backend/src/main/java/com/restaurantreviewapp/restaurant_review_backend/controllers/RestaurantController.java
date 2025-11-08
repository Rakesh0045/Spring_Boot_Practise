package com.restaurantreviewapp.restaurant_review_backend.controllers;

import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantSummaryDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import com.restaurantreviewapp.restaurant_review_backend.mappers.RestaurantMapper;
import com.restaurantreviewapp.restaurant_review_backend.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateUpdateRequestDto request){

        // Map RestaurantCreateUpdateRequestDto to RestaurantCreateUpdateRequest
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(request);

        // service returned Restaurant entity obj
        Restaurant restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);

        // Map the entity back to DTO and return
        RestaurantDto restaurantDto = restaurantMapper.toRestaurantDto(restaurant);

        return ResponseEntity.ok(restaurantDto);
    }

    @GetMapping
    public Page<RestaurantSummaryDto> searchRestaurants(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){

        Page<Restaurant> restaurants = restaurantService.searchRestaurants(
                q, minRating, latitude, longitude, radius, PageRequest.of(page - 1, size));

        return restaurants.map(restaurantMapper :: toSummaryDto);
    }
}
