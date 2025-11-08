package com.restaurantreviewapp.restaurant_review_backend.mappers;

import com.restaurantreviewapp.restaurant_review_backend.domain.dtos.PhotoDto;
import com.restaurantreviewapp.restaurant_review_backend.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDto toDto(Photo photo);

}
