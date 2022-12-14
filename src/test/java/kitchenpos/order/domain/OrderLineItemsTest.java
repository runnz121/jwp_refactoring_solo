package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class OrderLineItemsTest {
    private Order 주문;
    private OrderLineItem 뿌링클_세트_주문;

    @BeforeEach
    void setUp() {
        MenuGroup 뼈치킨 = new MenuGroup("뼈치킨");
        Menu 뿌링클_세트 = new Menu("뼈치킨 세트", BigDecimal.valueOf(22000), 뼈치킨);
        Product 뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        Product 치즈볼 = new Product("치즈볼", BigDecimal.valueOf(4000));
        OrderTable 주문테이블 = new OrderTable(1, false);
        주문 = Order.of(주문테이블.getId(), null);

        뿌링클_세트.create(Arrays.asList(new MenuProduct(뿌링클_세트, 뿌링클, 1L),
                new MenuProduct(뿌링클_세트, 치즈볼, 2L)));

        OrderMenu 뿌링클_세트_주문메뉴 = OrderMenu.of(뿌링클_세트);

        뿌링클_세트_주문 = new OrderLineItem(주문, 뿌링클_세트_주문메뉴, 1L);
    }

    @DisplayName("주문 상품을 추가한다.")
    @Test
    void 주문_상품_추가() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(주문, 뿌링클_세트_주문);

        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("이미 존재하는 주문 상품은 추가되지 않는다.")
    @Test
    void 이미_존재하는_주문_상품_추가() {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.addOrderLineItem(주문, 뿌링클_세트_주문);
        orderLineItems.addOrderLineItem(주문, 뿌링클_세트_주문);

        assertThat(orderLineItems.getOrderLineItems()).hasSize(1);
    }
}
