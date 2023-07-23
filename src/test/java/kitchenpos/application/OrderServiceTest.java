package kitchenpos.application;

import static kitchenpos.Fixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 정보를 생성하는지 확인하는 테스트")
    @Test
    void createTest() {

        List<OrderLineItem> orderLineItems = makeOrderLineItemList();
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId).collect(Collectors.toList());;
        long menuIdsSize = menuIds.size();
        Order order = makeOrder();
        OrderTable orderTable = makeOrderTable();

        // 없는 order
        Order notExistOrder = makeOrder();
        notExistOrder.setOrderLineItems(null);

        Order oneOrderLineItem = makeOrder();
        List<OrderLineItem> oneExist = new ArrayList<>();
        oneExist.add(makeOrderLineItem(3L));
        notExistOrder.setOrderLineItems(oneExist);
        List<Long> oneIds = oneExist.stream()
            .map(OrderLineItem::getMenuId).collect(Collectors.toList());;

        given(menuDao.countByIdIn(menuIds))
            .willReturn(menuIdsSize);
        given(menuDao.countByIdIn(oneIds))
            .willReturn(menuIdsSize);

        given(orderTableDao.findById(order.getOrderTableId()))
            .willReturn(Optional.ofNullable(orderTable));
        given(orderDao.save(order))
            .willReturn(order);

        // when
        orderService.create(order);

        assertAll(
            () -> assertThatThrownBy(() -> orderService.create(notExistOrder))
                .isExactlyInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> orderService.create(oneOrderLineItem))
                .isExactlyInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("주문 정보들을 갖고오는지 확인하는 테스트")
    @Test
    void listTest() {

    }

    @DisplayName("주문 정보 상태가 변경되는지 확인하는 테스트")
    @Test
    void changeOrderStatusTest() {

    }

}