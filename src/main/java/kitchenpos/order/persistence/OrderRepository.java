package kitchenpos.order.persistence;

import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findAllByOrderTableId(Long orderTableId);

    List<Order> findAllByOrderTableIdIn(List<Long> orderTableIds);
}