package com.restaurantreviewapp.restaurant_review_backend.services;

import com.restaurantreviewapp.restaurant_review_backend.domain.ReviewCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Review;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.User;

public interface ReviewService {
    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);
}

