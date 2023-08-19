package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private List<OrderTable> orderTables;
    private TableGroup givenTableGroup = new TableGroup();

    @BeforeEach
    void setUp() {
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);

        orderTables = Arrays.asList(orderTable1, orderTable2);

        givenTableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        // when
        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(orderTables);
        when(tableGroupDao.save(any(TableGroup.class)))
            .thenReturn(givenTableGroup);
    }

    @Test
    @DisplayName("테이블 그룹이 주어지면 저장 및 객체를 리턴하는지 확인하는 테스트")
    public void createTest() {

        TableGroup actual = tableGroupService.create(givenTableGroup);

        assertThat(actual).isEqualTo(givenTableGroup);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("테이블 그룹이 주문 테이블 없이 주어지는 경우 예외를 발생하는지 확인하는 테스트")
    public void throwExceptionWithoutOrderTable() {

        TableGroup givenTableGroup1 = new TableGroup();

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("테이블 그룹이 주문 테이블이 1개일 경우 예외를 발생하는지 확인하는 테스트")
    public void throwExceptionOneOrderTable() {

        TableGroup givenTableGroup1 = new TableGroup();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        List<OrderTable> orderSaveList = Arrays.asList(orderTable1);
        givenTableGroup1.setOrderTables(orderSaveList);

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("저장된 주문 테이블 갯수와 그룹 지정할 주문 테이블 갯수가 다를 경우 에러가 발생하는지 확인하는 테스트")
    public void throwExceptionDifferenceBetweenSaveOrderTableAndFindOrderTableCount() {

        TableGroup givenTableGroup1 = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        givenTableGroup1.setOrderTables(orderTables);

        // when -> saveOrderTables.size를 다르게 설정
        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("저장된 주문 테이블이 비어있지 않는다면 에러가 발생하는지 확인하는 테스트")
    public void throwExceptionWithEmptySavedOrderTable() {

        TableGroup givenTableGroup1 = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(false);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        givenTableGroup1.setOrderTables(orderTables);

        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("저장된 주문 테이블의 그룹아이디가 존재하면 에러가 발생하는지 확인하는 테스트")
    public void throwExceptionExistTableGroupId() {

        TableGroup givenTableGroup1 = new TableGroup();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(2L);

        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        givenTableGroup1.setOrderTables(orderTables);

        when(orderTableDao.findAllByIdIn(anyList()))
            .thenReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("unGroup메서드는 tableGroupId가 주어지면 변경 사항을 저장하는지 확인하는 테스트")
    void ungroupTest() {
        Long orderTableId = 1L;

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableDao.findAllByTableGroupId(orderTableId))
            .thenReturn(orderTables);

        tableGroupService.ungroup(orderTableId);

        verify(orderTableDao, times(2))
            .save(any(OrderTable.class));
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    @DisplayName("저장할 주문 테이블 상태가 완료가 아닌 경우 에러가 발생하는지 확인하는 테스트")
    void throwExceptionOrderStatusNotComplete() {

        Long orderTableId = 1L;

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        when(orderTableDao.findAllByTableGroupId(orderTableId))
            .thenReturn(orderTables);

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            anyList(), eq(Arrays.asList(OrderStatus.COOKING.name(),OrderStatus.MEAL.name()))
        )).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(orderTableId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}