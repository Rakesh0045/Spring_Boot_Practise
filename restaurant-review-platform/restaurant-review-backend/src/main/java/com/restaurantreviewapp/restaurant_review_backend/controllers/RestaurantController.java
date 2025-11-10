package com.restaurantreviewapp.restaurant_review_backend.controllers;

import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantSummaryDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.User;
import com.restaurantreviewapp.restaurant_review_backend.mappers.RestaurantMapper;
import com.restaurantreviewapp.restaurant_review_backend.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody RestaurantCreateUpdateRequestDto request){

        User user = jwtUser(jwt);

        // Map RestaurantCreateUpdateRequestDto to RestaurantCreateUpdateRequest
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(request);

        // service returned Restaurant entity obj
        Restaurant restaurant = restaurantService.createRestaurant(user, restaurantCreateUpdateRequest);

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

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable String restaurantId) {
        return restaurantService.getRestaurant(restaurantId)
                .map(restaurant -> ResponseEntity.ok(restaurantMapper.toRestaurantDto(restaurant)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> updateRestaurant(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String restaurantId,
            @Valid @RequestBody RestaurantCreateUpdateRequestDto request
    ){
        User user = jwtUser(jwt);

        // convert DTO to RestaurantCreateUpdateRequest
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(request);

        // Update the Restaurant
        Restaurant restaurant = restaurantService.updateRestaurant(user, restaurantId, restaurantCreateUpdateRequest);

        // Map back to DTO
        RestaurantDto restaurantDto = restaurantMapper.toRestaurantDto(restaurant);

        return ResponseEntity.ok(restaurantDto);
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<Void> deleteRestaurant(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String restaurantId) {

        User user = jwtUser(jwt);
        restaurantService.deleteRestaurant(user,restaurantId);
        return ResponseEntity.noContent().build();
    }

    // We need to extract user information from the JWT token to identify who is creating the review
    private User jwtUser(Jwt jwt){
        return User.builder()
                .id(jwt.getSubject()) // User's unique ID
                .username(jwt.getClaimAsString("preferred_username")) // User Name
                .givenName(jwt.getClaimAsString("given_name")) // First Name
                .familyName(jwt.getClaimAsString("family_name")) // Last Name
                .build();
    }


}
