package com.example.icecreamshop.consumer;

import com.example.icecreamshop.dto.OrderDto;
import com.example.icecreamshop.service.OrderProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);
    
    @Autowired
    private OrderProcessingService orderProcessingService;
    
    @KafkaListener(topics = "order-events", groupId = "ice-cream-group")
    public void handleOrderEvent(
            @Payload OrderDto orderDto,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        
        logger.info("Received order: {} from topic: {} partition: {} offset: {}", 
                   orderDto.getOrderId(), topic, partition, offset);
        
        try {
            // Process the order
            orderProcessingService.processOrder(orderDto);
            logger.info("Successfully processed order: {}", orderDto.getOrderId());
        } catch (Exception e) {
            logger.error("Error processing order event for order {}: {}", 
                        orderDto.getOrderId(), e.getMessage(), e);
            // In a real application, you might want to:
            // - Send to a dead letter queue
            // - Retry with backoff
            // - Alert monitoring systems
        }
    }
} 