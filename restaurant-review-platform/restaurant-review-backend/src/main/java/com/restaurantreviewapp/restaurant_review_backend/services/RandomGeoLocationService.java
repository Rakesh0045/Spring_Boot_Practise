package com.restaurantreviewapp.restaurant_review_backend.services;


import com.restaurantreviewapp.restaurant_review_backend.domain.GeoLocation;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Address;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGeoLocationService implements GeoLocationService {

    // Bounding box for the Cuttack-Bhubaneswar-Puri triangle
    private static final float MIN_LATITUDE = 19.80f;  // South of Puri
    private static final float MAX_LATITUDE = 20.50f;  // North of Cuttack
    private static final float MIN_LONGITUDE = 85.75f; // West of Bhubaneswar
    private static final float MAX_LONGITUDE = 86.00f; // East of Cuttack/Puri

    @Override
    public GeoLocation geoLocate(Address address) {
        Random random = new Random();


        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}