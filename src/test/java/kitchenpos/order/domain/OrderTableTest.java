package kitchenpos.order.domain;

import kitchenpos.JpaEntityTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("테이블 도메인 테스트")
public class OrderTableTest extends JpaEntityTest {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("테이블 생성 테스트")
    @Test
    void createOrderTable() {
        // given
        OrderTable orderTable = new OrderTable(4, true);

        // when
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        // then
        assertThat(savedOrderTable).isNotNull();
    }

    @DisplayName("테이블의 손님 숫자 변경")
    @Test
    void changeOrderTableNumberOfGuests() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(1, false));

        // when
        savedOrderTable.changeNumberOfGuests(2);

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("테이블의 손님 숫자 음수로 변경시 예외")
    @Test
    void changeOrderTableNumberOfGuestsException1() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(1, false));

        // when / then
        assertThatThrownBy(() -> savedOrderTable.changeNumberOfGuests(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 숫자 변경시 예외")
    @Test
    void changeOrderTableNumberOfGuestsException2() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(1, true));

        // when / then
        assertThatThrownBy(() -> savedOrderTable.changeNumberOfGuests(2))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("빈테이블 여부 변경 테스트")
    @Test
    void changeEmpty() {
        // given
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(1, true));

        // when
        savedOrderTable.changeEmpty(false);

        // then
        assertThat(savedOrderTable.isEmpty()).isFalse();
    }

    @DisplayName("빈테이블 여부 변경 테스트 예외 - 단체테이블에 속해있는 경우")
    @Test
    void changeEmptyException1() {
        // given
        OrderTable 테이블1 = orderTableRepository.save(new OrderTable(1, true));
        OrderTable 테이블2 = orderTableRepository.save(new OrderTable(1, true));
        TableGroup 단체테이블 = tableGroupRepository.save(new TableGroup(Lists.newArrayList(테이블1, 테이블2)));
        flushAndClear();

        // when / then
        assertThatThrownBy(() -> 테이블1.changeEmpty(true))
                .isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("빈테이블 여부 변경 테스트 예외 - 속해있는 주문이 요리 / 식사 상태중일 경우")
    @Test
    void changeEmptyException2() {
        // given
        Menu menu = createMenuFixture("순살후라이드");
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(1, false));
        Order order = orderRepository.save(new Order(savedOrderTable, Lists.newArrayList(new OrderLineItem(menu, 2L))));
        order.updateStatus(OrderStatus.MEAL);
        flushAndClear();

        // when
        OrderTable orderTable = orderTableRepository.getOne(savedOrderTable.getId());

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu createMenuFixture(String name) {
        Product product = new Product(name, BigDecimal.valueOf(10_000));
        productRepository.save(product);

        MenuGroup menuGroup = new MenuGroup("한마리치킨");
        Menu menu = new Menu(name, BigDecimal.valueOf(10_000), menuGroup, Lists.newArrayList(new MenuProduct(product, 1L)));
        return menuRepository.save(menu);
    }
}
