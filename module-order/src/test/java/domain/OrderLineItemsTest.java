package domain;

import kitchenpos.common.domain.Price;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {
    @DisplayName("메뉴 id를 추출할 수 있다")
    @Test
    void extractMenuIds() {
        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, "테스트", new Price(BigDecimal.TEN), 1),
                new OrderLineItem(2L, "테스트", new Price(BigDecimal.TEN), 1),
                new OrderLineItem(3L, "테스트", new Price(BigDecimal.TEN), 1));

        List<Long> ids = OrderLineItems.extractMenuIds(orderLineItems);

        assertThat(ids).hasSize(3);
        assertThat(ids).containsExactly(1L, 2L, 3L);
    }
}