package kitchenpos.table.fixture;

import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestOrderTableFactory {
    public static OrderTable 주문_테이블_조회됨(final Long id, final int numberOfGuests, final boolean empty) {
        return OrderTable.of(id, numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_생성됨(final Long id, final int numberOfGuests, final boolean empty) {
        return OrderTable.of(id, numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_테이블그룹_설정(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        return OrderTable.of(id, tableGroupId, numberOfGuests, empty);
    }

    public static List<OrderTable> 주문_테이블_목록_조회됨(int countProduct) {
        final List<OrderTable> orderTables = new ArrayList<>();
        for (int i = 1; i <= countProduct; i++) {
            orderTables.add(OrderTable.of(1L, 10, false));
        }
        return orderTables;
    }

    public static List<Long> 주문_테이블_ID_목록(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public static OrderTableRequest 주문_테이블_요청(final int numberOfGuests, final boolean empty) {
        return OrderTableRequest.of(numberOfGuests, empty);
    }

    public static void 주문_테이블_생성_확인됨(final OrderTableResponse actual, final OrderTable orderTable) {
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().toInt()),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    public static void 주문_테이블_목록_확인됨(final List<OrderTableResponse> actual, final List<OrderTable> orderTables) {
        assertThat(actual).hasSize(orderTables.size());
    }

    public static OrderTableRequest 주문_빈테이블_요청() {
        return OrderTableRequest.from(true);
    }

    public static OrderTableRequest 주문_손님_변경_테이블_요청(final int numberOfGuests) {
        return OrderTableRequest.from(numberOfGuests);
    }

    public static void 주문_빈테이블_확인됨(final OrderTableResponse actual, final OrderTable orderTable) {
        assertAll(
                () ->  assertThat(actual).isNotNull(),
                () ->  assertThat(actual.getId()).isEqualTo(orderTable.getId()),
                () ->  assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().toInt()),
                () ->  assertThat(actual.isEmpty()).isTrue()
        );
    }

    public static void 주문_손님변경테이블_확인됨(final OrderTableResponse actual, final int numberOfGuests) {
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    public static OrderTableResponse 주문_테이블_응답(final Long id, final int numberOfGuestsm, final boolean empty) {
        return OrderTableResponse.of(id, numberOfGuestsm, empty);
    }

    public static List<OrderLineItemResponse> 주문_상품아이템_응답(final Long seq, final Long menuId, final long quantity) {
        return Lists.newArrayList(OrderLineItemResponse.of(seq, menuId, quantity));
    }
}
