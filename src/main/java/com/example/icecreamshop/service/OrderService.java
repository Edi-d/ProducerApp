package com.example.icecreamshop.service;

import com.example.icecreamshop.dto.OrderDto;
import com.example.icecreamshop.dto.OrderItemDto;
import com.example.icecreamshop.entity.Order;
import com.example.icecreamshop.entity.OrderItem;
import com.example.icecreamshop.entity.OrderStatus;
import com.example.icecreamshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private KafkaTemplate<String, OrderDto> kafkaTemplate;
    
    private static final String ORDER_TOPIC = "order-events";
    
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // Generate unique order ID
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setStatus("PENDING");
        
        // Save to database
        Order order = convertToEntity(orderDto);
        order = orderRepository.save(order);
        
        // Convert back to DTO with updated info
        OrderDto savedOrderDto = convertToDto(order);
        
        // Send to Kafka
        kafkaTemplate.send(ORDER_TOPIC, savedOrderDto.getOrderId(), savedOrderDto);
        
        return savedOrderDto;
    }
    
    public Optional<OrderDto> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(this::convertToDto);
    }
    
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Optional<OrderDto> updateOrderStatus(String orderId, String status) {
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            order = orderRepository.save(order);
            
            OrderDto updatedOrderDto = convertToDto(order);
            
            // Send update to Kafka
            kafkaTemplate.send(ORDER_TOPIC, updatedOrderDto.getOrderId(), updatedOrderDto);
            
            return Optional.of(updatedOrderDto);
        }
        return Optional.empty();
    }
    
    // Conversion methods
    private Order convertToEntity(OrderDto dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setCustomerName(dto.getCustomerName());
        order.setCustomerEmail(dto.getCustomerEmail());
        order.setTotalAmount(dto.getTotalAmount());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(OrderStatus.valueOf(dto.getStatus().toUpperCase()));
        
        if (dto.getItems() != null) {
            List<OrderItem> items = dto.getItems().stream()
                    .map(itemDto -> convertToEntity(itemDto, order))
                    .collect(Collectors.toList());
            order.setItems(items);
        }
        
        return order;
    }
    
    private OrderItem convertToEntity(OrderItemDto dto, Order order) {
        OrderItem item = new OrderItem();
        item.setFlavor(dto.getFlavor());
        item.setSize(dto.getSize());
        item.setQuantity(dto.getQuantity());
        item.setPricePerUnit(dto.getPricePerUnit());
        item.setOrder(order);
        return item;
    }
    
    private OrderDto convertToDto(Order entity) {
        OrderDto dto = new OrderDto();
        dto.setOrderId(entity.getOrderId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerEmail(entity.getCustomerEmail());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setOrderDate(entity.getOrderDate());
        dto.setStatus(entity.getStatus().name());
        
        if (entity.getItems() != null) {
            List<OrderItemDto> items = entity.getItems().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            dto.setItems(items);
        }
        
        return dto;
    }
    
    private OrderItemDto convertToDto(OrderItem entity) {
        OrderItemDto dto = new OrderItemDto();
        dto.setFlavor(entity.getFlavor());
        dto.setSize(entity.getSize());
        dto.setQuantity(entity.getQuantity());
        dto.setPricePerUnit(entity.getPricePerUnit());
        return dto;
    }
} 