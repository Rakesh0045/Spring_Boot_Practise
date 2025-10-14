package com.tastytown.backend.service.impl;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tastytown.backend.dto.CartItemRequestDTO;
import com.tastytown.backend.dto.CartResponseDTO;
import com.tastytown.backend.entity.Cart;
import com.tastytown.backend.entity.CartItem;
import com.tastytown.backend.entity.User;
import com.tastytown.backend.helper.CartServiceHelper;
import com.tastytown.backend.helper.FoodServiceHelper;
import com.tastytown.backend.helper.UserServiceHelper;
import com.tastytown.backend.repository.CartRepository;
import com.tastytown.backend.service.ICartService;
import com.tastytown.backend.mapper.CartMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final UserServiceHelper userServiceHelper;
    private final CartServiceHelper cartServiceHelper;
    private final FoodServiceHelper foodServiceHelper;

    @Override
    public CartResponseDTO addItemToCart(String userId, CartItemRequestDTO cartItemRequestDTO) {

        var user = userServiceHelper.getUserById(userId);

        var cart = cartServiceHelper.getOrCreateCartForUser(user);

        var food = foodServiceHelper.getFoodById(cartItemRequestDTO.foodId());

        //check if item already exists in the cart
        Optional<CartItem> existingCartItemOpt = cart.getItems().stream()
                .filter(item -> item.getFood().getFoodId().equals(food.getFoodId()))
                .findFirst();

        if(existingCartItemOpt.isPresent()){
            //update quantity if cart item is present 
            CartItem existingCartItem = existingCartItemOpt.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequestDTO.quantity());
        }else{
            //create a new cart item if not exists
            CartItem newCartItem = CartItem.builder()
                            .cart(cart)
                            .food(food)
                            .quantity(cartItemRequestDTO.quantity())
                            .build();

            // store in cart
            cart.getItems().add(newCartItem);
        }        

        var savedCart = cartRepository.save(cart);

        return CartMapper.convertToCartResponseDTO(savedCart);

        /*
                [Client → POST /cart/add with JWT + foodId + quantity]
                                    ↓
                    JwtFilter: Validates token, extracts userId
                                    ↓
                    CartServiceImpl.addItemToCart(userId, dto)
                                    ↓
                        - Load user from DB
                        - Get or create cart
                        - Check if food already in cart
                            → Update quantity OR create new CartItem
                        - Save cart
                                    ↓
                    CartMapper → Converts to ResponseDTO
                                    ↓
                          Returns response to client
         */

    }

    @Override
    public CartResponseDTO getCartByUserId(String userId) {
        var user = userServiceHelper.getUserById(userId);
        var existingCart = cartServiceHelper.getOrCreateCartForUser(user);
        return CartMapper.convertToCartResponseDTO(existingCart);
    }

    @Override
    public CartResponseDTO updateItemQuantity(String userId, CartItemRequestDTO cartItemRequestDTO) {
        var user = userServiceHelper.getUserById(userId);
        var cart = cartServiceHelper.getOrCreateCartForUser(user);

        var cartItem = cartServiceHelper.getMatchedCartItemOfAnUser(cart, cartItemRequestDTO.foodId());
        
        if(cartItemRequestDTO.quantity() <= 0){
            cart.getItems().remove(cartItem);
        } else{
            cartItem.setQuantity(cartItemRequestDTO.quantity());
        }

        var savedCart = cartRepository.save(cart);
        return CartMapper.convertToCartResponseDTO(savedCart);

    }

    @Override
    public CartResponseDTO removeItemFromCart(String userId, String foodId) {
        
        User user = userServiceHelper.getUserById(userId);
        Cart cart = cartServiceHelper.getOrCreateCartForUser(user);
        var cartItem = cartServiceHelper.getMatchedCartItemOfAnUser(cart, foodId);

        cart.getItems().remove(cartItem);
        var savedCart = cartRepository.save(cart);

        return CartMapper.convertToCartResponseDTO(savedCart);
    }

    @Override
    public void clearCartItems(String userId) {
        var user = userServiceHelper.getUserById(userId);
        cartRepository.deleteByUser(user);
    }    
    
}
