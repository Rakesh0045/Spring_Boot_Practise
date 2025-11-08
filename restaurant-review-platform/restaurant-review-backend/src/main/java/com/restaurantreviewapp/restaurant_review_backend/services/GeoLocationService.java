package com.restaurantreviewapp.restaurant_review_backend.services;

import com.restaurantreviewapp.restaurant_review_backend.domain.GeoLocation;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Address;

public interface GeoLocationService {

    // Geolocation services convert postal addresses into geographical coordinates (latitude and longitude).
    GeoLocation geoLocate(Address address);

}
