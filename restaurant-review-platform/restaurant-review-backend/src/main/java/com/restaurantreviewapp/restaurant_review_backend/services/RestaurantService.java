package com.restaurantreviewapp.restaurant_review_backend.services;

import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantService {

    Restaurant createRestaurant(User author, RestaurantCreateUpdateRequest request);

    Page<Restaurant> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    Optional<Restaurant> getRestaurant(String id);

    Restaurant updateRestaurant(User author, String id, RestaurantCreateUpdateRequest restaurantCreateUpdateRequest);

    void deleteRestaurant(User author, String id);
}
