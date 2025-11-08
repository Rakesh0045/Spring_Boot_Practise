package com.restaurantreviewapp.restaurant_review_backend.manual;


import com.restaurantreviewapp.restaurant_review_backend.domain.RestaurantCreateUpdateRequest;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Address;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.OperatingHours;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Photo;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.TimeRange;
import com.restaurantreviewapp.restaurant_review_backend.services.PhotoService;
import com.restaurantreviewapp.restaurant_review_backend.services.RandomGeoLocationService;
import com.restaurantreviewapp.restaurant_review_backend.services.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RestaurantDataLoaderTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RandomGeoLocationService geoLocationService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    @Rollback(false) // Allow changes to persist
    public void createSampleRestaurants() throws Exception {
        List<RestaurantCreateUpdateRequest> restaurants = createRestaurantData();
        restaurants.forEach(restaurant -> {
            String fileName = restaurant.getPhotoIds().get(0);
            Resource resource = resourceLoader.getResource("classpath:testdata/" + fileName);
            MultipartFile multipartFile = null;
            try {
                multipartFile = new MockMultipartFile(
                        "file", // parameter name
                        fileName, // original filename
                        MediaType.IMAGE_PNG_VALUE,
                        resource.getInputStream()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            // Call the service method
            Photo uploadedPhoto = photoService.uploadPhoto(multipartFile);

            restaurant.setPhotoIds(List.of(uploadedPhoto.getUrl()));

            restaurantService.createRestaurant(restaurant);

            System.out.println("Created restaurant: " + restaurant.getName());
        });
    }

    private List<RestaurantCreateUpdateRequest> createRestaurantData() {
        return Arrays.asList(
                createRestaurant(
                        "The Golden Dragon",
                        "Chinese",
                        "+91 674 212 3456", // Bhubaneswar STD
                        createAddress("12", "Janpath", null, "Bhubaneswar", "Odisha", "751001", "India"),
                        createStandardOperatingHours("11:30", "23:00", "11:30", "23:30"),
                        "golden-dragon.png"
                ),
                createRestaurant(
                        "La Petite Maison",
                        "French",
                        "+91 671 223 4567", // Cuttack STD
                        createAddress("54", "Buxi Bazar", null, "Cuttack", "Odisha", "753001", "India"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "la-petit-maison.png"
                ),
                createRestaurant(
                        "Raj Pavilion",
                        "Indian",
                        "+91 6752 234 5678", // Puri STD
                        createAddress("27", "Grand Road", null, "Puri", "Odisha", "752001", "India"),
                        createStandardOperatingHours("12:00", "23:00", "12:00", "23:30"),
                        "raj-pavilion.png"
                ),
                createRestaurant(
                        "Sushi Master",
                        "Japanese",
                        "+91 674 245 6789",
                        createAddress("8", "KIIT Road", "Patia", "Bhubaneswar", "Odisha", "751024", "India"),
                        createStandardOperatingHours("11:30", "22:00", "11:30", "22:30"),
                        "sushi-master.png"
                ),
                createRestaurant(
                        "The Rustic Olive",
                        "Italian",
                        "+91 671 256 7890",
                        createAddress("92", "CDA Sector 6", null, "Cuttack", "Odisha", "753014", "India"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "rustic-olive.png"
                ),
                createRestaurant(
                        "El Toro",
                        "Spanish",
                        "+91 674 267 8901",
                        createAddress("15", "Saheed Nagar", null, "Bhubaneswar", "Odisha", "751007", "India"),
                        createStandardOperatingHours("12:00", "23:00", "12:00", "23:30"),
                        "el-toro.png"
                ),
                createRestaurant(
                        "The Greek House",
                        "Greek",
                        "+91 6752 278 9012",
                        createAddress("32", "CT Road", null, "Puri", "Odisha", "752002", "India"),
                        createStandardOperatingHours("12:00", "22:30", "12:00", "23:00"),
                        "greek-house.png"
                ),
                createRestaurant(
                        "Seoul Kitchen",
                        "Korean",
                        "+91 674 289 0123",
                        createAddress("71", "Infocity Avenue", null, "Bhubaneswar", "Odisha", "751024", "India"),
                        createStandardOperatingHours("11:30", "22:00", "11:30", "22:30"),
                        "seoul-kitchen.png"
                ),
                createRestaurant(
                        "Thai Orchid",
                        "Thai",
                        "+91 671 290 1234",
                        createAddress("45", "Link Road", null, "Cuttack", "Odisha", "753012", "India"),
                        createStandardOperatingHours("11:00", "22:30", "11:00", "23:00"),
                        "thai-orchid.png"
                ),
                createRestaurant(
                        "The Burger Joint",
                        "American",
                        "+91 6752 201 2345",
                        createAddress("88", "Sea Beach Road", null, "Puri", "Odisha", "752001", "India"),
                        createStandardOperatingHours("11:00", "23:00", "11:00", "23:30"),
                        "burger-joint.png"
                )
        );
    }

    private RestaurantCreateUpdateRequest createRestaurant(
            String name,
            String cuisineType,
            String contactInformation,
            Address address,
            OperatingHours operatingHours,
            String photoId
    ) {
        return RestaurantCreateUpdateRequest.builder()
                .name(name)
                .cuisineType(cuisineType)
                .contactInformation(contactInformation)
                .address(address)
                .operatingHours(operatingHours)
                .photoIds(List.of(photoId))
                .build();
    }

    private Address createAddress(
            String streetNumber,
            String streetName,
            String unit,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        Address address = new Address();
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setUnit(unit);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        return address;
    }

    private OperatingHours createStandardOperatingHours(
            String weekdayOpen,
            String weekdayClose,
            String weekendOpen,
            String weekendClose
    ) {
        TimeRange weekday = new TimeRange();
        weekday.setOpenTime(weekdayOpen);
        weekday.setCloseTime(weekdayClose);

        TimeRange weekend = new TimeRange();
        weekend.setOpenTime(weekendOpen);
        weekend.setCloseTime(weekendClose);

        OperatingHours hours = new OperatingHours();
        hours.setMonday(weekday);
        hours.setTuesday(weekday);
        hours.setWednesday(weekday);
        hours.setThursday(weekday);
        hours.setFriday(weekend);
        hours.setSaturday(weekend);
        hours.setSunday(weekend);

        return hours;
    }
}

