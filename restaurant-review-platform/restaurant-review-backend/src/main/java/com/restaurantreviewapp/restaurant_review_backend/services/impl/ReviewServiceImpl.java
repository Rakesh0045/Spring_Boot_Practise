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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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

    @Override
    public Page<Review> listReviews(String restaurantId, Pageable pageable) {

        // Get the restaurant or throw an exception if not found
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        // Create a list of reviews
        List<Review> reviews = new ArrayList<>(restaurant.getReviews());

        // Apply sorting based on the Pageable's Sort
        Sort sort = pageable.getSort();

        if(sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();

            // Custom sorting on basis of sort property provided in request
            Comparator<Review> reviewComparator = switch (property) {
                case "datePosted" -> Comparator.comparing(Review::getDatePosted);
                case "rating" -> Comparator.comparing(Review::getRating);
                default -> Comparator.comparing(Review::getDatePosted);
            };

            reviews.sort(isAscending ? reviewComparator : reviewComparator.reversed());
        } else {
            // Default sort by date descending
            reviews.sort(Comparator.comparing(Review::getDatePosted).reversed());
        }

        // Calculate pagination boundaries
        int start = (int) pageable.getOffset();

        // Handle empty pages
        if(start >= reviews.size()){
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        int end = Math.min((start + pageable.getPageSize()), reviews.size());

        // Create the page of reviews
        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());


        /*

                We implement manual pagination because reviews are stored as nested objects within the restaurant document.
                Using a separate index for reviews would eliminate the need for manual pagination, but would require:
                    Additional data synchronization logic
                    More complex data consistency checks
                    Increased storage requirements
               For our use case, the benefits of keeping reviews nested within restaurants outweigh the cost of manual pagination implementation.


         */

    }

    @Override
    public Optional<Review> getRestaurantReview(String restaurantId, String userReviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        return getReviewFromRestaurant(userReviewId, restaurant);
    }

    private static Optional<Review> getReviewFromRestaurant(String userReviewId, Restaurant restaurant) {
        return restaurant.getReviews().stream()
                .filter(review -> userReviewId.equals(review.getId()))
                .findFirst();
    }

    @Override
    public Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest review) {
        // Get the restaurant or throw an exception if not found
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        String authorId = author.getId();

        // Find the review and verify ownership
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if(!authorId.equals(existingReview.getWrittenBy().getId())){
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        // Verify the 48-hour edit window
        if (LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer be edited (48-hour limit exceeded)");
        }

        // Update the review content
        existingReview.setContent(review.getContent());
        existingReview.setRating(review.getRating());
        existingReview.setLastEdited(LocalDateTime.now());

        // Update photos
        existingReview.setPhotos(review.getPhotoIds().stream()
                .map(photoId ->
                            Photo.builder()
                            .url(photoId)
                            .uploadDate(LocalDateTime.now())
                            .build())
                            .toList());

        // Recalculate restaurant's average rating
        updateRestaurantAverageRatings(restaurant);

        // Extract the other reviews
        List<Review> updatedReviews = restaurant.getReviews().stream()
                .filter(r -> !reviewId.equals(r.getId()))
                .collect(Collectors.toList());

        // Add existing review updated one to the List
        updatedReviews.add(existingReview);

        restaurant.setReviews(updatedReviews);

        restaurantRepository.save(restaurant);

        return existingReview;

    }

    @Override
    public void deleteReview(User author, String restaurantId, String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        String authorId = author.getId();

        // Find the review and verify ownership
        Review existingReview = getReviewFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if(!authorId.equals(existingReview.getWrittenBy().getId())){
            throw new ReviewNotAllowedException("Cannot delete another user's review");
        }

        // Filter out the review with the matching ID
        List<Review> filteredReviews = restaurant.getReviews().stream()
                .filter(review -> !reviewId.equals(review.getId()))
                .toList();

        // Update the restaurant's reviews
        restaurant.setReviews(filteredReviews);

        // Update the restaurant's average rating
        updateRestaurantAverageRatings(restaurant);

        // Save the updated restaurant
        restaurantRepository.save(restaurant);
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
