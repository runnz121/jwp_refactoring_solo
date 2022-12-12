package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    @DisplayName("주문 생성 시, 주문테이블이 비어있다면 예외가 발생한다.")
    @Test
    void orderTableEmptyException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), true);

        // when & then
        assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
    }

    @DisplayName("계산완료 상태에 대한 유효성을 확인할 수 있다.")
    @Test
    void statusShouldCompleteException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> order.validateOrderStatusShouldComplete())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_NOT_COMPLETE.getMessage());
    }

    @DisplayName("주문의 상태를 수정할 수 있다.")
    @Test
    void updateOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        // when
        order.updateOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산완료 상태의 주문은 상태를 수정할 수 없다.")
    @Test
    void updateOrderStatusException() {
        // given
        OrderTable orderTable = new OrderTable(new NumberOfGuests(4), false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> order.updateOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_STATUS_COMPLETE.getMessage());
    }
}