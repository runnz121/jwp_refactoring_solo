package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.order.service.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;

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

        List<Order> givenOrders;

        @BeforeEach
        void setUp() {
            Order order = new Order();
            givenOrders = Collections.singletonList(order);
            when(orderService.list())
                .thenReturn(givenOrders);
        }

        @DisplayName("200 과 메뉴 목록을 응답")
        @Test
        void responseWithOrders() throws Exception {
            mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                    objectMapper.writeValueAsString(givenOrders)
                ));
        }

    }

    @Nested
    @DisplayName("PUT /api/orders/{orderId}/order-status")
    class GetOrder {

        @Nested
        @DisplayName("갱신할 주문과 주문 정보가 주어지는 경우")
        class changeOrderStatus {
            Long givenOrderId = 1L;
            Order givenOrder = new Order();

            @BeforeEach
            void setUp() {
                givenOrder.setOrderStatus(OrderStatus.COOKING.name());

                when(orderService.changeOrderStatus(anyLong(), any(Order.class)))
                    .thenReturn(givenOrder);
            }

            @DisplayName("200 Ok와 상태 갱신된 주문 정보를 응답한다.")
            @Test
            void responseAndUpdateOrder() throws Exception{
                mockMvc.perform(put("/api/orders/{orderId}/order-status", givenOrder)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(givenOrder)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(givenOrder)
                    ));
            }
        }
    }
}