package com.naman.capstone.mapper;

import com.naman.capstone.dto.response.OrderItemResponseDTO;
import com.naman.capstone.dto.response.OrderResponseDTO;
import com.naman.capstone.entity.Order;
import com.naman.capstone.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class for converting order entity and DTO
 */
public class OrderMapper {

    /**
     * Converts Order entity to OrderResponseDTO
     */
    public static OrderResponseDTO toDTO(Order order) {

        List<OrderItemResponseDTO> itemsDTO = new ArrayList<>();

        /**
         *  converts each order item in OrderItemResponse dto
         */
        for (OrderItem item : order.getItems()) {
            OrderItemResponseDTO dto = new OrderItemResponseDTO(
                    item.getId(),
                    item.getMenuItem().getId(),
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotalPrice()
            );
            itemsDTO.add(dto);
        }

        /**
         * returns OrderResponse DTO object
         */
        return new OrderResponseDTO(
                order.getId(),
                order.getRestaurant().getName(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderTime(),
                itemsDTO
        );
    }
}