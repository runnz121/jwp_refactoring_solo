package kitchenpos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Product;

public class Fixture {

    public static Menu makeMenu() {
        Menu sampleMenu = new Menu();

        sampleMenu.setId(1L);
        sampleMenu.setName("sample_Menu");
        sampleMenu.setName("sample_name");
        sampleMenu.setPrice(new BigDecimal(1));
        sampleMenu.setMenuGroupId(1L);
        sampleMenu.setMenuProducts(makeListMenuProduct());

        return sampleMenu;
    }

    public static MenuGroup makeMenuGroup() {
        MenuGroup sample = new MenuGroup();

        sample.setId(1L);
        sample.setName("test");

        return sample;
    }

    public static List<MenuProduct> makeListMenuProduct() {
        List<MenuProduct> list = new ArrayList<>();

        MenuProduct menu1 = makeMenuProduct();
        MenuProduct menu2 = makeMenuProduct();

        list.add(menu1);
        list.add(menu2);

        return list;
    }

    public static MenuProduct makeMenuProduct() {
        MenuProduct menuProduct = new MenuProduct();

        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        return menuProduct;
    }

    public static Product makeProduct() {

        Product product = new Product();

        product.setId(1L);
        product.setName("test");
        product.setPrice(new BigDecimal(10000));

        return product;
    }

    public static List<Menu> makeMenuList() {
        List<Menu> list = new ArrayList<>();

        Menu menu1 = makeMenu();
        Menu menu2 = makeMenu();

        list.add(menu1);
        list.add(menu2);

        return list;
    }

    public static Order makeOrder() {
        Order order = new Order();

        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus("ORDER");
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(makeOrderLineItemList());

        return order;
    }

    public static List<Order> makeOrderList() {
        List<Order> orderList = new ArrayList<>();

        Order order1 = makeOrder();
        Order order2 = makeOrder();

        orderList.add(order1);
        orderList.add(order2);

        return orderList;
    }

    public static OrderLineItem makeOrderLineItem(Long sampleSeq) {
        OrderLineItem orderLineItem = new OrderLineItem();

        orderLineItem.setSeq(sampleSeq);
        orderLineItem.setOrderId(sampleSeq);
        orderLineItem.setMenuId(sampleSeq);
        orderLineItem.setQuantity(1);

        return orderLineItem;
    }

    public static List<OrderLineItem> makeOrderLineItemList() {
        List<OrderLineItem> lineItems = new ArrayList<>();

        OrderLineItem orderLineItem1 = makeOrderLineItem(1L);
        OrderLineItem orderLineItem2 = makeOrderLineItem(2L);

        lineItems.add(orderLineItem1);
        lineItems.add(orderLineItem2);

        return lineItems;
    }

    public static OrderTable makeOrderTable() {
        OrderTable orderTable = new OrderTable();

        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(false);

        return orderTable;
    }
}
