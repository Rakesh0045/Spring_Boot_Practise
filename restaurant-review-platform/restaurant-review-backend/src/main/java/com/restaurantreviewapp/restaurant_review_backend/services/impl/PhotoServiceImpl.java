package com.restaurantreviewapp.restaurant_review_backend.services.impl;

import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Photo;
import com.restaurantreviewapp.restaurant_review_backend.services.PhotoService;
import com.restaurantreviewapp.restaurant_review_backend.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;

    @Override
    public Photo uploadPhoto(MultipartFile file) {

        //Generate a unique ID for the photo
        String photoId = UUID.randomUUID().toString();

        // Store the file and get its URL
        String url = storageService.store(file, photoId);

        // Create and populate Photo Entity
        return Photo.builder()
                .url(url)
                .uploadDate(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return storageService.loadAsResource(id);
    }
}
