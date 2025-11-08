package com.restaurantreviewapp.restaurant_review_backend.domain;

import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Address;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.OperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCreateUpdateRequest {
    private String name; // Restaurant's name
    private String cuisineType; // Type of cuisine served
    private String contactInformation; // Contact details
    private Address address; // Physical location
    private OperatingHours operatingHours; // Opening hours
    private List<String> photoIds; // References to uploaded photos
}
