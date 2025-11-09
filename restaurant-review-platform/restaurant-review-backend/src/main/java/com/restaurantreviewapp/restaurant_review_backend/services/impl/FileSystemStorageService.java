package com.restaurantreviewapp.restaurant_review_backend.services.impl;

import com.restaurantreviewapp.restaurant_review_backend.exceptions.StorageException;
import com.restaurantreviewapp.restaurant_review_backend.services.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    // @Value annotation lets us configure the storage location through application properties, with a default value of "uploads" if not specified.
    @Value("${app.storage.location:uploads}")
    private String storageLocation;

    // root location of storage directory
    private Path rootLocation;

    // After bean creation of this class, initialize the storage location
    @PostConstruct
    public void init(){
        rootLocation = Paths.get(storageLocation);
        try{
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }


    @Override
    public String store(MultipartFile file, String filename) {

        // Check for empty files
        try{
            if(file.isEmpty()){
                throw new StorageException("cannot save an empty file");
            }

            // Create final filename with extension
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String finalFileName = filename + "." + extension;

            // Resolve and normalize the destination path
            Path destinationFile = rootLocation
                    .resolve(Paths.get(finalFileName))
                    .normalize()
                    .toAbsolutePath();

            // Security check to prevent directory traversal
            if(!destinationFile.getParent().equals(rootLocation.toAbsolutePath())){
                throw new StorageException("Cannot store file outside specified directory");
            }

            // Copy the file to the destination
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return finalFileName;
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Optional<Resource> loadAsResource(String filename) {
        try {
            // Resolve the file path relative to our root location
            Path file = rootLocation.resolve(filename);

            // Create a Resource object from the file path
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return Optional.of(resource);
            } else {
                return Optional.empty();
            }
        }catch (MalformedURLException e){
            log.warn("Could not read file: "+filename, e);
            return Optional.empty();
        }
    }

}
