package com.example.icecreamshop.service;

import com.example.icecreamshop.dto.OrderDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingService.class);
    
    public void processOrder(OrderDto orderDto) {
        logger.info("Processing order: {}", orderDto.getOrderId());
        
        try {
            // Simulate order processing logic
            switch (orderDto.getStatus().toUpperCase()) {
                case "PENDING":
                    logger.info("Order {} is pending - starting processing", orderDto.getOrderId());
                    // Here you could add logic to:
                    // - Check inventory
                    // - Validate payment
                    // - Send confirmation email
                    break;
                    
                case "PROCESSING":
                    logger.info("Order {} is being processed", orderDto.getOrderId());
                    // Here you could add logic to:
                    // - Prepare the ice cream
                    // - Update inventory
                    // - Notify kitchen
                    break;
                    
                case "COMPLETED":
                    logger.info("Order {} has been completed", orderDto.getOrderId());
                    // Here you could add logic to:
                    // - Send completion notification
                    // - Update metrics
                    // - Archive order
                    break;
                    
                case "CANCELLED":
                    logger.info("Order {} has been cancelled", orderDto.getOrderId());
                    // Here you could add logic to:
                    // - Refund payment
                    // - Restore inventory
                    // - Send cancellation notification
                    break;
                    
                default:
                    logger.warn("Unknown order status: {} for order {}", orderDto.getStatus(), orderDto.getOrderId());
            }
            
            // Log order details
            logger.info("Customer: {}, Total: ${}, Items: {}", 
                       orderDto.getCustomerName(), 
                       orderDto.getTotalAmount(), 
                       orderDto.getItems() != null ? orderDto.getItems().size() : 0);
                       
        } catch (Exception e) {
            logger.error("Error processing order {}: {}", orderDto.getOrderId(), e.getMessage(), e);
        }
    }
} 