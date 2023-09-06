package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
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
        @Nested
        @DisplayName("저장된 주문테이블이 테이블 그룹에 포함된 아이디일 경우")
        class saveOrderTableHasGroupId {
            final Long givenGroupId = 1L;
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                Long saveTableGroupId = 1L;
                OrderTable saveOrderTable = new OrderTable();
                saveOrderTable.setTableGroupId(saveTableGroupId);
                when(orderTableDao.findById(givenGroupId))
                    .thenReturn(Optional.of(saveOrderTable));
            }

            @Test
            @DisplayName("에러를 발생한다")
            void throwException() {
                assertThatThrownBy(() -> tableService.changeEmpty(givenGroupId, givenOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @MockitoSettings(strictness = Strictness.WARN)
        @DisplayName("조회한 orderTableId의 orderTable의 상태가 완료 상태가 아닌 경우")
        class findOrderTableStatusIsNotCompletion {
            final Long givenOrderTableId = 1L;
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                    any(Long.class), eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
                )).thenReturn(true);
            }

            @Test
            @DisplayName("에러를 발생한다")
            void throwException() {

                assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTableId, givenOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }

    @Nested
    @DisplayName("changeNumberOfGuestsMethod는")
    class changeNumberOfGuestsMethodTest {

        @Nested
        @DisplayName("변경할 방문 손님수와 주문테이블 id가 주어지면")
        class GivenOrderTableGroupIdAndChangeNumberOfGuests {

            final Long givenOrderTableId = 1L;
            final OrderTable givenOrderTable = new OrderTable();
            int updateGuestNumber = 0;

            @BeforeEach
            void setUp() {
                givenOrderTable.setNumberOfGuests(updateGuestNumber);
            }

            @Test
            @DisplayName("예외를 발생한다")
            void updateNumberOfGuestsAndReturnOrderTable() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("저장된 테이블이 비어있다면")
        class EmptySavedOrderTable {

            final Long givenOrderTableId = 1L;
            final OrderTable givenOrderTable = new OrderTable();

            @BeforeEach
            void setUp() {
                givenOrderTable.setEmpty(true);
            }

            @Test
            @DisplayName("예외를 발생한다")
            void tableEmpty() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTableId, givenOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}