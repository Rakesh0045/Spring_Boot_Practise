package com.restaurantreviewapp.restaurant_review_backend.mappers;

import com.restaurantreviewapp.restaurant_review_backend.domain.ReviewCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.ReviewCreateUpdateRequestDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.ReviewDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto);
    ReviewDto toReviewDto(Review review);
}
