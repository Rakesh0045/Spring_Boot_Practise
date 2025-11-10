package com.restaurantreviewapp.restaurant_review_backend.controllers;

import com.restaurantreviewapp.restaurant_review_backend.domain.ReviewCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.ReviewCreateUpdateRequestDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.ReviewDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Review;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.User;
import com.restaurantreviewapp.restaurant_review_backend.mappers.ReviewMapper;
import com.restaurantreviewapp.restaurant_review_backend.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt){

        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewCreateUpdateRequest(dto);

        User user = jwtUser(jwt);

        Review review = reviewService.createReview(user, restaurantId, reviewCreateUpdateRequest);

        ReviewDto reviewDto = reviewMapper.toReviewDto(review);

        return new ResponseEntity<>(reviewDto,HttpStatus.CREATED);
    }

    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC) Pageable pageable

            ) {

        return reviewService
                .listReviews(restaurantId, pageable)
                .map(reviewMapper::toReviewDto);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getRestaurantReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId) {
        return reviewService
                .getRestaurantReview(restaurantId, reviewId)
                .map(reviewMapper::toReviewDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto dto,
            @AuthenticationPrincipal Jwt jwt
    ){

        // Convert the DTO to domain object
        ReviewCreateUpdateRequest reviewCreateUpdateRequest = reviewMapper.toReviewCreateUpdateRequest(dto);

        // Extract user information from JWT
        User user = jwtUser(jwt);

        // Call service to perform update
        Review updatedReview = reviewService.updateReview(user, restaurantId, reviewId, reviewCreateUpdateRequest);

        // Return updated review
        return ResponseEntity.ok(reviewMapper.toReviewDto(updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = jwtUser(jwt);
        reviewService.deleteReview(user,restaurantId, reviewId);
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
