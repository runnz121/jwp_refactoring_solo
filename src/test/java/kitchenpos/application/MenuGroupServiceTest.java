package kitchenpos.application;

import static kitchenpos.Fixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.order.repository.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴그륩이 생성되는지 확인하는 테스트")
    void createTest() {

        MenuGroup sample = makeMenuGroup();
        // when
        menuGroupService.create(sample);

        assertAll(
            () -> verify(menuGroupDao, times(1)).save(any(MenuGroup.class))
        );
    }

    @Test
    @DisplayName("메뉴그룹 리스트를 불러오는지 확인하는 테스트")
    void listTest() {

        menuGroupService.list();

        assertAll(
            () -> verify(menuGroupDao, times(1)).findAll()
        );

    }



}