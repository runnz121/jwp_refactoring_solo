package kitchenpos.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import net.bytebuddy.description.NamedElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@WebMvcTest(OrderRestController.class)
@DisplayName("OrderRestControllerTest")
class OrderRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Nested
    @DisplayName("POST /api/orders")
    class Create {

        Order givenOrder = new Order();

        @BeforeEach
        void setUp() {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            orderLineItem.setQuantity(1);

            givenOrder.setOrderTableId(1L);
            givenOrder.setOrderLineItems(Collections.singletonList(orderLineItem));

            when(orderService.create(any(Order.class)))
                .thenReturn(givenOrder);
        }

        @DisplayName("201 생성된 주문을 응답한다")
        @Test
        void responseCreateWithOrder() throws Exception {
            mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(givenOrder)))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                    objectMapper.writeValueAsString(givenOrder)
                ));
        }
    }

    @Nested
    @DisplayName("GET /api/orders")
    class GetOrders {

    }

    @Nested
    @DisplayName("PUT /api/orders/{orderId}/order-status")
    class GetOrder {

    }
}