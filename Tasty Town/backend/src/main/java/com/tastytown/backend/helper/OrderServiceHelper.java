package com.tastytown.backend.helper;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tastytown.backend.constants.OrderStatus;
import com.tastytown.backend.dto.BillingInfoDTO;
import com.tastytown.backend.entity.Cart;
import com.tastytown.backend.entity.CartItem;
import com.tastytown.backend.entity.Order;
import com.tastytown.backend.entity.OrderItem;
import com.tastytown.backend.entity.User;
import com.tastytown.backend.repository.OrderRepository;
import com.tastytown.backend.utils.OrderCodeGenerator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderServiceHelper {

    private final OrderRepository orderRepository;
    
    @Value("${order.delivery.fee}")
    private double deliveryFee;

    @Value("${order.tax.rate}")
    private double taxRate;

    public Order createOrderFromCart(Cart cart, BillingInfoDTO billingInfo, User user) {
        var order = new Order();
        order.setUser(user);
        order.setOrderDateTime(LocalDateTime.now());
        order.setOrderCode(OrderCodeGenerator.generateOrderCode());
        order.setOrderStatus(OrderStatus.FOOD_PREPARING);
        order.setContactInfo(formatContactInfo(billingInfo));
        order.setAddressInfo(formatAddressInfo(billingInfo));

        double subTotal = 0;

        for(CartItem cartItem : cart.getItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFoodPrice(cartItem.getFood().getFoodPrice());
            orderItem.setFoodName(cartItem.getFood().getFoodName());
            orderItem.setQuantity(cartItem.getQuantity());

            subTotal += cartItem.getFood().getFoodPrice() * cartItem.getQuantity();
            order.getOrderItems().add(orderItem);
        }

        double totalAmount = subTotal + deliveryFee + (subTotal * taxRate);
        order.setTotalAmount(totalAmount);

        var savedOrder = orderRepository.save(order);

        return savedOrder;

    }

    public String formatContactInfo(BillingInfoDTO billingInfo){
        return String.join(",", billingInfo.fullName(), billingInfo.email(), billingInfo.phoneNumber());
    }

    public String formatAddressInfo(BillingInfoDTO billingInfo){
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(billingInfo.address());
        joiner.add(billingInfo.city());
        joiner.add(billingInfo.state());
        joiner.add(billingInfo.zip());

        return joiner.toString();
    }
    
}

