package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    protected OrderLineItem(){}


    public OrderLineItem(Long seq, Order order, Menu menu, long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Order order, Menu menu) {
        return new OrderLineItem(null, order, menu, 0);
    }

    public static OrderLineItem of(long seq, Order order, Menu menu, long quantity) {
        return new OrderLineItem(seq, order, menu, quantity);
    }

    public static OrderLineItem of(Order order, Menu target, long quantity) {
        return new OrderLineItem(null, order, target, quantity);
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public Long getSeq() {
        return this.seq;
    }

    public Long getOrderId() {
        return this.order.getId();
    }

    public Long getMenuId() {
        return this.menu.getId();
    }
}
