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

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@WebMvcTest(TableRestControllerTest.class)
@DisplayName("TableRestControllerTest")
class TableRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @Nested
    @DisplayName("POST /api/tables")
    class Create {

        @Nested
        @DisplayName("OrderTable이 주어지면")
        class CreateOrderTable {

            OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(0);
                givenOrderTable.setEmpty(true);

                when(tableService.create(any(OrderTable.class)))
                    .thenReturn(givenOrderTable);
            }

            @DisplayName("OrderTable을 저장한다")
            @Test
            void createTOrderTable() throws Exception {
                mockMvc.perform(post("/api/tables")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(givenOrderTable)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(givenOrderTable)
                    ));
            }
        }
    }

    @Nested
    @DisplayName("GET /api/tables")
    class GetLists {

        @Nested
        @DisplayName("OrderTable 리스틀 갖고온다")
        class CreateOrderTable {

            List<OrderTable> givenOrderTables;

            @BeforeEach
            void setUp() {
                OrderTable orderTable = new OrderTable();
                givenOrderTables = Collections.singletonList(orderTable);
                when(tableService.list())
                    .thenReturn(givenOrderTables);
            }

            @DisplayName("리스트를 반환한다 ")
            @Test
            void createTOrderTable() throws Exception {

                mockMvc.perform(get("/api/tables"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(givenOrderTables)
                    ));
            }
        }

        @Nested
        @DisplayName("PUT /api/tables/{orderTableId}/empty")
        class ChangeEmpty {

            @Nested
            @DisplayName("orderTableId와 OrderTable이 주어지면 ")
            class CreateOrderTable {

                Long givenOrderTableId = 1L;
                OrderTable givenOrderTable = new OrderTable();

                @BeforeEach
                void setUp() {
                    givenOrderTable.setEmpty(true);

                    when(tableService.changeEmpty(anyLong(), any(OrderTable.class)))
                        .thenReturn(givenOrderTable);
                }

                @DisplayName("주어진 OrderTable 상태를 빈 상태로 변환한다. ")
                @Test
                void createTOrderTable() throws Exception {
                    mockMvc.perform(put("/api/tables/{orderTableId}/empty", givenOrderTableId)
                            .content(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(givenOrderTable)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(
                            objectMapper.writeValueAsString(givenOrderTable)
                        ));
                }
            }

            @Nested
            @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests")
            class ChangeNumberOfGuests {

                @Nested
                @DisplayName("OrderTable과 OrderTableId가 주어지면")
                class CreateOrderTable {

                    Long givenOrderTableId = 1L;
                    OrderTable givenOrderTable = new OrderTable();

                    @BeforeEach
                    void setUp() {
                        givenOrderTable.setNumberOfGuests(2);

                        when(tableService.changeNumberOfGuests(anyLong(), any(OrderTable.class)))
                            .thenReturn(givenOrderTable);
                    }

                    @DisplayName("OrderTable의 인원수를 변경한다.")
                    @Test
                    void createTOrderTable() throws Exception {

                        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", givenOrderTableId)
                            .content(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(givenOrderTable)))
                            .andExpect(status().isOk())
                            .andExpect(content().string(
                                objectMapper.writeValueAsString(givenOrderTable)
                            ));
                    }
                }
            }
        }
    }
}