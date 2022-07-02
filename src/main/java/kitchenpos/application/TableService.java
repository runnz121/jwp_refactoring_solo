package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.validator.OrderValidator;
import kitchenpos.dto.OrderTableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    @Autowired
    public TableService(OrderTableRepository orderTableRepository, OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        return orderTableRepository.save(new OrderTable(request.getNumberOfGuests(), request.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                    .orElseThrow(NoSuchElementException::new);

        orderValidator.hasOrderStatusInCookingOrMeal(orderTable);

        orderTable.changeEmpty(request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                    .orElseThrow(NoSuchElementException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return orderTableRepository.save(orderTable);
    }
}
