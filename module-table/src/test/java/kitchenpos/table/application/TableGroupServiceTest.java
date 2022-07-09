package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_리스트_생성;
import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static kitchenpos.table.__fixture__.TableGroupTestFixture.빈_테이블_그룹_생성;
import static kitchenpos.table.__fixture__.TableGroupTestFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.infra.OrderTableRepository;
import kitchenpos.table.infra.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableGroupService 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;
    private OrderTable 주문_테이블3;
    private TableGroup 테이블_그룹;

    @BeforeEach
    void setUp() {
        주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        주문_테이블2 = 주문_테이블_생성(2L, null, 3, false);
        주문_테이블3 = 주문_테이블_생성(3L, null, 3, false);

        테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));
    }

    @Test
    @DisplayName("단체 테이블 생성 시 주문 테이블이 비었을 경우 Exception")
    public void createOrderTableListAreEmptyException() {
        final TableGroup 빈_테이블_그룹 = 빈_테이블_그룹_생성(1L, LocalDateTime.now());

        assertThatThrownBy(() -> tableGroupService.create(빈_테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 생성 시 주문 테이블이 2개 미만일 경우 Exception")
    public void createOrderTableListAreLessThanTwoException() {
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블));

        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 생성 시 주문 테이블 수와 저장된 단체 테이블의 주문 테이블 수가 다를 경우 Exception")
    public void createOrderTableCountsAreDifferentException() {
        final List<Long> 주문_테이블_아이디_리스트 = 테이블_그룹.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> 저장된_주문_테이블_리스트 = 주문_테이블_리스트_생성(주문_테이블, 주문_테이블2, 주문_테이블3);

        given(orderTableRepository.findAllByIdIn(주문_테이블_아이디_리스트)).willReturn(저장된_주문_테이블_리스트);

        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 생성 시 주문 테이블이 비어있지 않으면 Exception")
    public void createOrderTableIsNotEmptyException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, false);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 생성 시 이미 단체 지정된 테이블은 단체로 지정 불가")
    public void createOrderTableIsAlreadyGroupException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, false);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 생성")
    public void create() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, true);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, true);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        final List<Long> 주문_테이블_아이디_리스트 = 테이블_그룹.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> 저장된_주문_테이블_리스트 = 주문_테이블_리스트_생성(주문_테이블, 주문_테이블2);

        given(orderTableRepository.findAllByIdIn(주문_테이블_아이디_리스트)).willReturn(저장된_주문_테이블_리스트);
        given(tableGroupRepository.save(테이블_그룹)).willReturn(테이블_그룹);
        assertThat(tableGroupService.create(테이블_그룹).getId()).isEqualTo(테이블_그룹.getId());
    }

    @Test
    @DisplayName("단체 테이블 해제 시 주문 상태가 COOKING이거나 MEAL이면 Exception")
    public void ungroupOrderStatusIsCookingOrMealException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, true);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, true);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        final List<Long> 주문_테이블_아이디_리스트 = 테이블_그룹.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(tableGroupRepository.findById(테이블_그룹.getId())).willReturn(Optional.ofNullable(테이블_그룹));
        given(orderTableRepository.findAllByTableGroup(테이블_그룹)).willReturn(테이블_그룹.getOrderTables());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(주문_테이블_아이디_리스트,
                Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 해제")
    public void ungroup() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, true);
        final OrderTable 주문_테이블2 = 주문_테이블_생성(2L, null, 4, true);
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));

        final List<Long> 주문_테이블_아이디_리스트 = 테이블_그룹.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        given(tableGroupRepository.findById(테이블_그룹.getId())).willReturn(Optional.ofNullable(테이블_그룹));
        given(orderTableRepository.findAllByTableGroup(테이블_그룹)).willReturn(테이블_그룹.getOrderTables());
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(주문_테이블_아이디_리스트,
                Arrays.asList(COOKING, MEAL))).willReturn(false);

        tableGroupService.ungroup(테이블_그룹.getId());
        assertThat(주문_테이블.getTableGroupId()).isNull();
        assertThat(주문_테이블2.getTableGroupId()).isNull();
    }
}