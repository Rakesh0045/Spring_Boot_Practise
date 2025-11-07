package com.tastytown.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tastytown.backend.dto.CartItemRequestDTO;
import com.tastytown.backend.dto.CartResponseDTO;
import com.tastytown.backend.service.ICartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173"
})
public class CartController {

    private final ICartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(@RequestAttribute String userId,
            @RequestBody CartItemRequestDTO requestDTO) {
        // return new ResponseEntity<>(cartService.addItemToCart(userId, requestDTO),
        // HttpStatus.CREATED);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItemToCart(userId, requestDTO));
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCartByUserId(@RequestAttribute String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<CartResponseDTO> updateCartItemQuantity(@RequestAttribute String userId,
            @RequestBody CartItemRequestDTO requestDTO) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, requestDTO));
    }

    @DeleteMapping("/item/{foodId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(@RequestAttribute String userId,
            @PathVariable String foodId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cartService.removeItemFromCart(userId, foodId));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> clearCartItems(@RequestAttribute String userId) {
        cartService.clearCartItems(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}