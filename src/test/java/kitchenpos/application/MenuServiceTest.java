package kitchenpos.application;

import static kitchenpos.Fixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성하는지 확인하는 테스트")
    void create() {

        Long findId = 1L;
        Menu sampleMenu = makeMenu();

        given(menuGroupDao.existsById(findId))
            .willReturn(true);
        given(productDao.findById(findId))
            .willReturn();

        menuService.create()

    }

    @Test
    @DisplayName("메뉴 리스트를 불러 오는지 확인하는 테스트 ")
    void list() {

    }




}