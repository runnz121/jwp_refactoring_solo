package kitchenpos.application;

import static kitchenpos.Fixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

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
        Long notExistId = 2L;
        Menu sampleMenu = makeMenu();
        // 존재 하지 않는 그룹아이디
        Menu sampleMenu2 = makeMenu();
        sampleMenu2.setMenuGroupId(notExistId);

        MenuProduct sampleMenuProduct = sampleMenu.getMenuProducts().get(0);
        Product sampleProduct = makeProduct();

        // 이거 일 떄만 허용
        given(menuGroupDao.existsById(findId))
            .willReturn(true);
        given(productDao.findById(findId))
            .willReturn(Optional.of(sampleProduct));
        given(menuDao.save(sampleMenu))
            .willReturn(sampleMenu);

        menuService.create(sampleMenu);

        assertAll(
            () -> assertThatThrownBy(() -> menuService.create(sampleMenu2))
                .isExactlyInstanceOf(IllegalArgumentException.class),
            // 리스트 길이가 2
            () -> verify(productDao, times(2)).findById(findId),
            () -> verify(menuDao, times(1)).save(sampleMenu),
            // 반환되는 객체가 갖고있는 객체를 반환
            () -> verify(menuProductDao, times(1))
                .save(sampleMenuProduct)
        );

    }

    @Test
    @DisplayName("메뉴 리스트를 불러 오는지 확인하는 테스트 ")
    void list() {

        List<Menu> list = makeMenuList();
        List<MenuProduct> menuProducts = makeListMenuProduct();

        given(menuDao.findAll())
            .willReturn(list);
        given(menuProductDao.findAllByMenuId(1L))
            .willReturn(menuProducts);

        menuService.list();

        assertAll(
            () -> verify(menuDao, times(1)).findAll(),
            () -> verify(menuProductDao, times(2)).findAllByMenuId(1L)
        );
    }
}