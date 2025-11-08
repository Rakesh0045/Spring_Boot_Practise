package com.restaurantreviewapp.restaurant_review_backend.services;

import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {

    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);

    Page<Restaurant> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

}
