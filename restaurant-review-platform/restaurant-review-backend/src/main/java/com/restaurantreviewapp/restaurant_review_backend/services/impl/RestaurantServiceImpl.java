package com.restaurantreviewapp.restaurant_review_backend.services.impl;

import com.restaurantreviewapp.restaurant_review_backend.domain.GeoLocation;
import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Address;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Photo;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Restaurant;
import com.restaurantreviewapp.restaurant_review_backend.repositories.RestaurantRepository;
import com.restaurantreviewapp.restaurant_review_backend.services.GeoLocationService;
import com.restaurantreviewapp.restaurant_review_backend.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {

        // Extract the address and convert it into coordinates

        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        // transform the provided photo URLs into Photo entities, setting the upload date to the current time.

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(photoUrl -> Photo.builder()
                .url(photoUrl)
                .uploadDate(LocalDateTime.now())
                .build()).toList();

        // build the restaurant entity object and save

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .contactInformation(request.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();

        return restaurantRepository.save(restaurant);

    }

    @Override
    public Page<Restaurant> searchRestaurants(String query, Float minRating, Float latitude,
                                              Float longitude, Float radius, Pageable pageable) {

        if(null != minRating && (null == query || query.isEmpty())){
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        // Normalize min rating to be used in other queries
        Float searchMinRating = minRating == null ? 0f : minRating;

        // If there's a text, search query
        if (query != null && !query.trim().isEmpty()) {
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        // If there's a location search
        if (latitude != null && longitude != null && radius != null) {
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        // Otherwise we'll perform a non-location search
        return restaurantRepository.findAll(pageable);

    }
}
