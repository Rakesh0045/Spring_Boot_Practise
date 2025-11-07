package com.tastytown.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastytown.backend.dto.FoodRequestDTO;
import com.tastytown.backend.dto.FoodResponseDTO;
import com.tastytown.backend.service.IFoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/foods")
@RequiredArgsConstructor
@Tag(name = "Food API", description = "This controller manages crud operations for category entities")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173"
})
public class FoodController {

    private final IFoodService foodService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Food Created")
    @Operation(summary = "Create a new Food")
    public ResponseEntity<FoodResponseDTO> saveFood(
            /* @RequestPart FoodRequestDTO requestDTO */ @RequestPart String rawJson,
            @RequestPart MultipartFile foodImage) throws IOException {

        // RequestPart treat both params(FoodRequestDTO & Image) as single Json object

        // Convert Json obj to Java obj
        FoodRequestDTO requestDTO = objectMapper.readValue(rawJson, FoodRequestDTO.class);

        return new ResponseEntity<>(foodService.createFood(requestDTO, foodImage), HttpStatus.CREATED);

    }

    @GetMapping
    @ApiResponse(description = "Foods extracted")
    @Operation(summary = "Extracts all foods")
    public ResponseEntity<List<FoodResponseDTO>> getAllFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    @GetMapping("/{foodId}")
    @Operation(summary = "Get food by id")
    @ApiResponse(description = "Extract food by id ")
    public ResponseEntity<FoodResponseDTO> getFoodById(@PathVariable String foodId) {
        return ResponseEntity.ok(foodService.getFoodById(foodId));
    }

    @DeleteMapping("/{foodId}")
    @ApiResponse(description = "Food deleted")
    @Operation(summary = "Delete food by id")
    public ResponseEntity<FoodResponseDTO> deleteFoodById(@PathVariable String foodId) throws IOException {
        foodService.deleteFoodById(foodId);
        return ResponseEntity.noContent().build(); // ✅ standard for DELETE
    }

    @PutMapping("/{foodId}")
    @ApiResponse(responseCode = "201", description = "Food updated")
    @Operation(summary = "Update food by id")
    public ResponseEntity<FoodResponseDTO> updateFoodById(@PathVariable String foodId, @RequestPart String rawJson,
            @RequestPart(required = false) MultipartFile foodImage) throws IOException {

        FoodRequestDTO requestDTO = objectMapper.readValue(rawJson, FoodRequestDTO.class);
        return ResponseEntity.ok(foodService.updateFoodById(foodId, requestDTO, foodImage));
    }

    @GetMapping("/paginated-foods")
    @ApiResponse(description = "Display foods by pagination")
    @Operation(summary = "Display foods by pagination")
    public ResponseEntity<Page<FoodResponseDTO>> getPaginatedFoods(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "8") int pageSize,
            @RequestParam(required = false, defaultValue = "all") String categoryId,
            @RequestParam(required = false, defaultValue = "all") String search) {
        return ResponseEntity.ok(foodService.getPaginatedFoods(pageNumber, pageSize, categoryId, search));
    }

}
