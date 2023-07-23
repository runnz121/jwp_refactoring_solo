package kitchenpos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

public class Fixture {

    public static Menu makeMenu() {
        Menu sampleMenu = new Menu();

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
}
