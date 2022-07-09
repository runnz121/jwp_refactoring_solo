package kitchenpos.util;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.table.domain.OrderTable;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

public class TestFixture {

    public static Product 후라이드_상품_생성() {
        return Product.of(1L, "후라이드", new BigDecimal(16000));
    }

    public static Product 양념치킨_상품_생성() {
        return Product.of((long)2, "양념치킨", new BigDecimal(16000));
    }

    public static MenuGroup 한마리_메뉴_그룹_생성() {
        return MenuGroup.of("한마리메뉴");
    }

    public static MenuGroup 두마리_메뉴_그룹_생성() {
        return MenuGroup.of("두마리메뉴");
    }

    public static MenuProduct 후라이드_메뉴_상품_생성() {
        return MenuProduct.of(1L, 1);
    }

    public static MenuProduct 양념_메뉴_상품_생성(Long productId) {
        return MenuProduct.of(2L, 2);
    }

    public static Menu 후라이드_치킨_메뉴_생성(Long menuGroupId, List<MenuProduct> menuProducs) {
        return Menu.of("후라이드치킨", new BigDecimal(16000), 1L, menuProducs);
    }

    public static Menu 양념_치킨_메뉴_생성(Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of("양념치킨", new BigDecimal(32000), 1L, menuProducts);
    }

    public static OrderTable 주문테이블_1_생성() {
        return OrderTable.of(1L, 1, false);
    }

    public static OrderTable 빈_주문테이블_1_생성() {
        return OrderTable.of(null, 2, true);
    }

    public static OrderTable 빈_주문테이블_2_생성() {
        return OrderTable.of(null, 3, true);
    }

    public static OrderTable 빈_주문테이블_3_생성() {
        return OrderTable.of(null, 3, true);
    }

    public static TableGroup 단체지정_1_생성(List<OrderTable> orderTables) {
        return TableGroup.of(orderTables);
    }

    public static void 주문항목_생성(OrderLineItem orderLineItem, Order order) {
        try{
            Field[] fields = orderLineItem.getClass().getDeclaredFields();
            for(Field f : fields){
                f.setAccessible(true);  //private 변수를 public 변수로 변경
                if (f.getName().equals("id"))  f.set(orderLineItem,  1L);
                if (f.getName().equals("order")) f.set(orderLineItem, order);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void 주문_테이블_생성(OrderTable orderTable, Long id) {
        try{
            Field[] fields = orderTable.getClass().getDeclaredFields();
            for(Field f : fields){
                f.setAccessible(true);  //private 변수를 public 변수로 변경
                if (f.getName().equals("id"))  f.set(orderTable,  id);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void 메뉴_생성(Menu menu) {
        try{
            Field[] fields = menu.getClass().getDeclaredFields();
            for(Field f : fields){
                f.setAccessible(true);  //private 변수를 public 변수로 변경
                if (f.getName().equals("id"))  f.set(menu,  1L);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}