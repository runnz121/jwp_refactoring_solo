package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Nested
    @DisplayName("createMethod는 ")
    class CreateMethodTest {
        @Nested
        @DisplayName("주문테이블이 주어지면")
        class WhenGivenOrderTable {
            final OrderTable givenOrderTable = new OrderTable();
            @BeforeEach
            void setUp() {
                when(orderTableDao.save(any(OrderTable.class)))
                    .thenReturn(givenOrderTable);
            }
            @Test
            @DisplayName("주문테이블을 저장하고 리턴한다")
            void saveOrderTableAndReturn() {
                final OrderTable actual = tableService.create(givenOrderTable);

                assertThat(actual).isEqualTo(givenOrderTable);
            }
        }
    }

    @Nested
    @DisplayName("listMethod는")
    class ListMethodTest {
        @Nested
        @DisplayName("주문 테이블 목록이 있다면")
        class WhenOrderTablesExist {
            final OrderTable orderTable1 = new OrderTable();
            final OrderTable orderTable2 = new OrderTable();

            @BeforeEach
            void setUp() {
                when(orderTableDao.findAll())
                    .thenReturn(Arrays.asList(orderTable1, orderTable2));
            }

            @Test
            @DisplayName("주문 테이블 리스트를 리턴한다")
            void returnOrderTableList() {
                List<OrderTable> actual = tableService.list();

                assertThat(actual).containsExactly(orderTable1, orderTable2);
            }
        }
    }

    @Nested
    @DisplayName("changeEmtpyMethod는")
    class ChangeEmtpyTest {

        @Nested
        @DisplayName("orderTableId와 OrderTable이 주어지면")
        class GivenOrderTableIdAndOrderTable {
            final Long givenOrderTableId = 1L;
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setEmpty(true);

                OrderTable saveOrderTable = new OrderTable();
                saveOrderTable.setEmpty(false);
                when(orderTableDao.findById(anyLong()))
                    .thenReturn(Optional.of(saveOrderTable));

                OrderTable updateOrderTable = new OrderTable();
                updateOrderTable.setEmpty(givenOrderTable.isEmpty()); // true
                when(orderTableDao.save(any(OrderTable.class)))
                    .thenReturn(updateOrderTable);
            }

            @Test
            @DisplayName("비어있는 상태의 주문 테이블을 리턴")
            void returnEmptyStatusOrderTable() {
                final OrderTable actual = tableService.changeEmpty(givenOrderTableId, givenOrderTable);

                assertThat(actual.isEmpty()).isTrue();
            }
        }
    }
}