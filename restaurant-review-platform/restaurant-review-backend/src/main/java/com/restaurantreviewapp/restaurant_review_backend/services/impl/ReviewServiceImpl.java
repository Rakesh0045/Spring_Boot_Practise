package com.restaurantreviewapp.restaurant_review_backend.services.impl;

import com.restaurantreviewapp.restaurant_review_backend.domain.ReviewCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Photo;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Review;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.User;
import com.restaurantreviewapp.restaurant_review_backend.exceptions.RestaurantNotFoundException;
import com.restaurantreviewapp.restaurant_review_backend.exceptions.ReviewNotAllowedException;
import com.restaurantreviewapp.restaurant_review_backend.repositories.RestaurantRepository;
import com.restaurantreviewapp.restaurant_review_backend.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review) {

        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        // Check if user has already reviewed this restaurant
        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if(hasExistingReview){
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");
        }


        LocalDateTime now = LocalDateTime.now();

        // Create photos
        List<Photo> photos = review.getPhotoIds().stream().map(photoUrl -> {
            return Photo.builder()
                    .url(photoUrl)
                    .uploadDate(now)
                    .build();

        }).toList();


        // Create review
        String userReviewId = UUID.randomUUID().toString();
        Review reviewToCreate = Review.builder()
                .id(userReviewId)
                .content(review.getContent())
                .rating(review.getRating())
                .photos(photos)
                .datePosted(now)
                .lastEdited(now)
                .writtenBy(author)
                .build();

        // Add review to the restaurant
        restaurant.getReviews().add(reviewToCreate);

        // Update avg ratings of the restaurant
        updateRestaurantAverageRatings(restaurant);

        // Save restaurant with new review
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Return the newly created review
        return savedRestaurant.getReviews().stream()
                .filter(r -> userReviewId.equals(r.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error retrieving created review"));

    }

    private Restaurant getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    private void updateRestaurantAverageRatings(Restaurant restaurant){
        List<Review> reviews = restaurant.getReviews();

        if(reviews.isEmpty()){
            restaurant.setAverageRating(0.0f);
        }else{
            double averageRating = reviews.stream().mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            restaurant.setAverageRating((float) averageRating);
        }
    }
}
