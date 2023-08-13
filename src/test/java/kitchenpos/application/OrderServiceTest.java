package kitchenpos.application;

import static kitchenpos.Fixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import kitchenpos.domain.OrderStatus;
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
            () -> verify(orderTableDao).findById(order.getId()),
            () -> verify(orderDao).save(order),
            () -> verify(orderLineItemDao, times(2)).save(any())
        );
    }

    @DisplayName("주문 정보들을 갖고오는지 확인하는 테스트")
    @Test
    void listTest() {

        List<Order> orders = new ArrayList<>();
        Order order1 = makeOrder();
        Order order2 = makeOrder();

        orders.add(order1);
        orders.add(order2);

        given(orderDao.findAll())
            .willReturn(Arrays.asList(order1, order2));

        // when
        orderService.list();

        assertThat(orders).containsExactly(order1, order2);
    }

    @DisplayName("주문 정보 상태가 변경되는지 확인하는 테스트")
    @Test
    void changeOrderStatusTest() {
        final Long orderId = 1L;
        final Order order = makeOrder();
        order.setOrderStatus(OrderStatus.MEAL.name());

        final Long notExistOrderId = 3L;
        final Order notExistOrder = new Order();
        notExistOrder.setId(notExistOrderId);
        notExistOrder.setOrderStatus(OrderStatus.COMPLETION.name());


        given(orderDao.findById(1L))
            .willReturn(Optional.of(order));
        given(orderDao.save(any(Order.class)))
            .willReturn(order);

        // when
        orderService.changeOrderStatus(orderId, order);

        assertAll(
            () -> assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, notExistOrder))
                .isExactlyInstanceOf(IllegalArgumentException.class),
            () -> verify(orderLineItemDao).findAllByOrderId(orderId)
        );
    }
}