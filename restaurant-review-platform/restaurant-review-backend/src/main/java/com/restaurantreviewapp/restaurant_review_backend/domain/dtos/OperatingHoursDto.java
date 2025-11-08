package com.restaurantreviewapp.restaurant_review_backend.domain.dtos;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperatingHoursDto {

    @Valid
    private TimeRangeDto monday;

    @Valid
    private TimeRangeDto tuesday;

    @Valid
    private TimeRangeDto wednesday;

    @Valid
    private TimeRangeDto thursday;

    private TimeRangeDto friday;

    @Valid
    private TimeRangeDto saturday;

    @Valid
    private TimeRangeDto sunday;

}
