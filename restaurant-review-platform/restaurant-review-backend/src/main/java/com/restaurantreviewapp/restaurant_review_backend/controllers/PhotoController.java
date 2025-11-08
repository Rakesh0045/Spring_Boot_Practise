package com.restaurantreviewapp.restaurant_review_backend.controllers;

import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.PhotoDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Photo;
import com.restaurantreviewapp.restaurant_review_backend.mappers.PhotoMapper;
import com.restaurantreviewapp.restaurant_review_backend.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;

    @PostMapping
    public PhotoDto uploadPhoto(@RequestParam("file")MultipartFile file){ //@RequestParam("file") binds the uploaded file to the MultipartFile parameter
        Photo savedPhoto = photoService.uploadPhoto(file);
        return photoMapper.toDto(savedPhoto);
    }

    // The {id:.+} path variable pattern allows for file extensions in the ID
    @GetMapping(path = "/{id:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String id){
        return photoService.getPhotoAsResource(id).map(photo ->
                ResponseEntity.ok()
                        .contentType(
                                MediaTypeFactory.getMediaType(photo)  // MediaTypeFactory helps determine the correct content type for the photo
                                        .orElse(MediaType.APPLICATION_OCTET_STREAM)
                        )
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline") // The Content-Disposition: inline header tells browsers to display the image rather than download it
                        .body(photo)
        ).orElse((ResponseEntity.notFound().build()));
    }

}
