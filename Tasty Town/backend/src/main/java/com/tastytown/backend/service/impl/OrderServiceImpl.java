package com.tastytown.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.tastytown.backend.constants.OrderStatus;
import com.tastytown.backend.dto.BillingInfoDTO;
import com.tastytown.backend.dto.OrderDTO;
import com.tastytown.backend.entity.Cart;
import com.tastytown.backend.entity.CartItem;
import com.tastytown.backend.entity.Order;
import com.tastytown.backend.entity.OrderItem;
import com.tastytown.backend.entity.User;
import com.tastytown.backend.helper.CartServiceHelper;
import com.tastytown.backend.helper.OrderServiceHelper;
import com.tastytown.backend.helper.UserServiceHelper;
import com.tastytown.backend.mapper.OrderMapper;
import com.tastytown.backend.repository.OrderRepository;
import com.tastytown.backend.service.IOrderService;
import com.tastytown.backend.utils.OrderCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final UserServiceHelper userServiceHelper;
    private final CartServiceHelper cartServiceHelper;
    private final OrderServiceHelper orderServiceHelper;
    private final OrderRepository orderRepository;

    @Value("${order.delivery.fee}")
    private double deliveryFee;

    @Value("${order.tax.rate}")
    private double taxRate;

    @Override
    public OrderDTO placeOrder(BillingInfoDTO billingInfoDTO, String userId) {
        var user = userServiceHelper.getUserById(userId);
        var cart = cartServiceHelper.getCartByUser(user);

        if(cart.getItems().isEmpty()){
            throw new IllegalStateException("Cart is Empty");
        }

        var order = orderServiceHelper.createOrderFromCart(cart, billingInfoDTO, user);
        
        return OrderMapper.convertToOrderDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(String userId) {
       var user = userServiceHelper.getUserById(userId);

        return orderRepository.findAllByUser(user, Sort.by(Direction.DESC, "orderDateTime"))
                .stream().map(OrderMapper :: convertToOrderDTO).toList();
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        var orders = orderRepository.findAll(Sort.by(Direction.DESC, "orderDateTime"));
        return orders.stream().map(OrderMapper :: convertToOrderDTO ).toList();
    }

    @Override
    public OrderDTO updateOrderStatus(String orderCode, OrderStatus status) {
        var order = orderRepository.findByOrderCode(orderCode)
            .orElseThrow(() -> new NoSuchElementException("Order Not Found with code:- " + orderCode));
        
        order.setOrderStatus(status);
        var savedOrder = orderRepository.save(order); 
        return OrderMapper.convertToOrderDTO(savedOrder);
    }

}


    
