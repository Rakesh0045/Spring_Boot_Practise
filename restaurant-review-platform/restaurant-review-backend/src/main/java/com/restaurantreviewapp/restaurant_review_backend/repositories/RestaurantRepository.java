package com.restaurantreviewapp.restaurant_review_backend.domain.repository;

import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {
}
