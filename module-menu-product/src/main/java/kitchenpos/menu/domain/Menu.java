package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuName name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(MenuName name, Price price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, MenuName name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, MenuName name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        this.menuProducts.addMenu(this);
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.from(menuProducts);
        this.menuProducts.addMenu(this);
    }

    public boolean isMoreExpensiveThanMenuProductsPrice(Price sumOfMenuProductPrice) {
        return price.compareTo(sumOfMenuProductPrice) > 0;
    }

    public Long getId() {
        return id;
    }

    public MenuName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public int getMenuProductsSize() {
        return menuProducts.getSize();
    }
}