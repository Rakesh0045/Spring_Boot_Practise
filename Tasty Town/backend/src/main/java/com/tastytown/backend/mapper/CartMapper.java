package com.tastytown.backend.mapper;

import java.util.List;

import com.tastytown.backend.dto.CartItemResponseDTO;
import com.tastytown.backend.dto.CartResponseDTO;
import com.tastytown.backend.entity.Cart;

public class CartMapper {
    
    private CartMapper(){

    }

    public static CartResponseDTO convertToCartResponseDTO(Cart cart){
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(
                                    item.getFood().getFoodId(),
                                    item.getQuantity(),
                                    item.getFood().getFoodName(),
                                    item.getFood().getCategory().getCategoryName(),
                                    item.getFood().getFoodPrice())
        ).toList();

        return new CartResponseDTO(items);
    }
}
