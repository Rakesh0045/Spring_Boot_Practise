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
