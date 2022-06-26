package kitchenpos.application.helper;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.product.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTestHelper {
    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    public MenuGroup 메뉴그룹_생성됨(String name) {
        return menuGroupService.create(MenuGroupFixtureFactory.createMenuGroup(name));
    }

    public Product 상품_생성됨(String name, int price) {
        return productService.create(ProductFixtureFactory.createProduct(name, price));
    }

    public Menu 메뉴_생성됨(MenuGroup menuGroup, String menuName, int menuPrice, List<MenuProduct> menuProducts) {
        return menuService.create(MenuFixtureFactory.createMenu(menuGroup, menuName, menuPrice, menuProducts));
    }

    public TableGroup 테이블그룹_지정됨(int numberOfTables) {
        List<OrderTable> orderTables = IntStream.range(0, numberOfTables)
                .mapToObj((index) -> 빈테이블_생성됨()).collect(toList());
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(orderTables));
    }

    public TableGroup 테이블그룹_지정됨(OrderTable... orderTables) {
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(Arrays.asList(orderTables)));
    }

    public OrderTable 빈테이블_생성됨() {
        return tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
    }

    public OrderTable 비어있지않은테이블로_변경(Long orderTableId) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(false);
        return tableService.changeEmpty(orderTableId, param);
    }

    public OrderTable 비어있지않은테이블_생성됨(int numberOfGuests) {
        return tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
    }

    public OrderTable 빈테이블로_변경(Long orderTableId) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeEmptyState(true);
        return tableService.changeEmpty(orderTableId, param);
    }

    public OrderTable 테이블_인원수_변경(Long orderTableId, int updatedNumberOfGuests) {
        OrderTable param = OrderTableFixtureFactory.createParamForChangeNumberOfGuests(updatedNumberOfGuests);
        return tableService.changeNumberOfGuests(orderTableId, param);
    }

    public Order 주문_생성됨(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return orderService.create(OrderFixtureFactory.createOrder(orderTableId, orderLineItems));
    }

    public Order 주문상태_변경(Long orderId, OrderStatus orderStatus) {
        Order param = OrderFixtureFactory.createParamForUpdateStatus(orderStatus);
        return orderService.changeOrderStatus(orderId, param);
    }


}
