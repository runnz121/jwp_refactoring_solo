package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.application.TableGroupService;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블3;
    private OrderTable 주문테이블4;
    private TableGroup 단체그룹;

    private List<OrderTable> 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(1L, 4, true);
        주문테이블2 = new OrderTable(2L, 4, true);

        주문테이블3 = new OrderTable(3L, 2, true);
        주문테이블4 = new OrderTable(4L, 2, true);

        단체그룹 = new TableGroup(1L, LocalDateTime.now());

        주문_테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);
        단체그룹.group(주문_테이블_목록);
    }

    @DisplayName("주문 테이블 생성 테스트")
    @Test
    void createTableGroupTest() {
        // given
        when(orderTableRepository.findAllById(Arrays.asList(주문테이블3.getId(), 주문테이블4.getId())))
                .thenReturn(Arrays.asList(주문테이블3, 주문테이블4));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(단체그룹);

        TableGroupResponse response =
                tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블3.getId(), 주문테이블4.getId())));

        assertAll(
                () -> assertThat(단체그룹.getId()).isEqualTo(response.getId()),
                () -> assertThat(단체그룹.getOrderTables().size()).isEqualTo(response.getOrderTables().size())
        );
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 등록된 주문 테이블이 아닌 경우")
    @Test
    void createTableGroupTest2() {
        // given
        when(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1));

        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 주문 테이블이 2개 미만인 경우")
    @Test
    void createTableGroupTest3() {
        // given
        when(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId()))).thenReturn(Arrays.asList(주문테이블1));

        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 빈 주문 테이블이 존재하는 경우")
    @Test
    void createTableGroupTest4() {
        // given
        when(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 주문 테이블이 다른 단체에 속해 있는 경우")
    @Test
    void createTableGroupTest5() {
        // given
        when(orderTableRepository.findAllById(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제 테스트")
    @Test
    void ungroupTest() {
        // given
        when(tableGroupRepository.findById(단체그룹.getId())).thenReturn(Optional.of(단체그룹));
        when(orderRepository.findAllByOrderTableIdIn(단체그룹.getOrderTableIds()))
                .thenReturn(Arrays.asList(new Order(주문테이블1, OrderStatus.COMPLETION), new Order(주문테이블2, OrderStatus.COMPLETION)));

        // when
        tableGroupService.ungroup(단체그룹.getId());

        // then
        assertThat(주문_테이블_목록.stream().allMatch(orderTable -> orderTable.getTableGroup() == null)).isTrue();
    }

    @DisplayName("단체 지정 해제 테스트 - 단체 내 주문 테이블들의 상태가 조리, 식사일 경우")
    @Test
    void ungroupTest2() {
        // given
        when(tableGroupRepository.findById(단체그룹.getId())).thenReturn(Optional.of(단체그룹));
        when(orderRepository.findAllByOrderTableIdIn(단체그룹.getOrderTableIds()))
                .thenReturn(Arrays.asList(new Order(주문테이블1, OrderStatus.COMPLETION), new Order(주문테이블2, OrderStatus.MEAL)));

        // when & then
        Assertions.assertThatThrownBy(
                () -> tableGroupService.ungroup(단체그룹.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

}