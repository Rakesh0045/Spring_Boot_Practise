package com.restaurantreviewapp.restaurant_review_backend.services;

import com.restaurantreviewapp.restaurant_review_backend.domain.GeoLocation;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Address;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeoLocationService implements GeoLocationService {

    // TIGHT Bounding box just for Bhubaneswar city
    private static final float MIN_LATITUDE = 20.23f;   // South Bhubaneswar (near Dhauli)
    private static final float MAX_LATITUDE = 20.37f;   // North Bhubaneswar (near Patia/KIIT)
    private static final float MIN_LONGITUDE = 85.78f;  // West Bhubaneswar (near Khandagiri)
    private static final float MAX_LONGITUDE = 85.88f;  // East Bhubaneswar (near Rasulgarh)

    @Override
    public GeoLocation geoLocate(Address address) {
        // This service IGNORES the input address and generates a random
        // coordinate within the Bhubaneswar city limits.

        Random random = new Random();

        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}