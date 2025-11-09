package com.restaurantreviewapp.restaurant_review_backend.mappers;

import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.GeoPointDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.RestaurantSummaryDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateRequestDto dto);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantDto toRestaurantDto(Restaurant restaurant);

    // toGeoPointDto method converts Elasticsearch's GeoPoint type to our DTO format
    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantSummaryDto toSummaryDto(Restaurant restaurant);

    // map the totalReviews field to this method which returns the size of List
    @Named("populateTotalReviews")
    default Integer populateTotalReviews(List <Review> reviews) {
        return reviews.size();
    }


}
