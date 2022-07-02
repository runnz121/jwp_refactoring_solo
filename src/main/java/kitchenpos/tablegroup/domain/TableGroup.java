package kitchenpos.tablegroup.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public static final String ERROR_ORDER_TABLE_NOT_EMPTY = "주문테이블은 비어있어야 합니다.";
    public static final String ERROR_ORDER_TABLE_DUPLICATED = "주문테이블은 중복될 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GROUPED = "주문테이블은 그룹에 지정되어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_TOO_SMALL = "주문테이블의 개수는 %d 미만일 수 없습니다.";
    public static final String ERROR_ORDER_INVALID_STATUS = "주문의 상태는 %s일 수 없습니다.";
    public static final int MIN_ORDER_TABLE_NUMBER = 2;

    protected TableGroup() {
    }

    private TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        assignOrderTables(orderTables);
    }

    public static TableGroup of(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }

    private void assignOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.assignTableGroup(this));
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesDuplicated(orderTables);
        validateOrderTablesNotGrouped(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < MIN_ORDER_TABLE_NUMBER) {
            throw new IllegalOrderTableException(
                    String.format(ERROR_ORDER_TABLE_TOO_SMALL, MIN_ORDER_TABLE_NUMBER)
            );
        }
    }

    private void validateOrderTablesDuplicated(List<OrderTable> orderTables) {
        if(orderTables.size() != orderTables.stream().distinct().count()){
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_DUPLICATED);
        }
    }

    private void validateOrderTablesNotGrouped(List<OrderTable> orderTables) {
        if (orderTables.stream().
                anyMatch(orderTable -> orderTable.isGrouped())) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_GROUPED);
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().
                anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_NOT_EMPTY);
        }
    }

    public void ungroup(List<Order> orders) {
        validateOrdersToUngroup(orders);
        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroup();
        }
    }

    private void validateOrdersToUngroup(List<Order> orders) {
        if (orders.stream()
                .anyMatch(order -> order.isCooking() || order.isEating())) {
            throw new IllegalOrderException(
                    String.format(ERROR_ORDER_INVALID_STATUS, OrderStatus.COOKING + " " + OrderStatus.MEAL)
            );
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
