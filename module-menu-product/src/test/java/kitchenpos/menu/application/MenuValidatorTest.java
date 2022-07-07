package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.common.Price;
import kitchenpos.fixture.ProductFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.common.Quantity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuValidator menuValidator;

    @Test
    void 메뉴_가격_검증() {

        // given
        Product 토마토 = ProductFactory.createProduct(1L, "토마토", 1000);
        Product 양상추 = ProductFactory.createProduct(2L, "양상추", 1000);
        Menu 빅맥 = new Menu(1L, MenuName.from("빅맥"), Price.from(10000), 1L,
                MenuProducts.from(Arrays.asList(new MenuProduct(토마토.getId(), Quantity.from(1)),
                        new MenuProduct(양상추.getId(), Quantity.from(5)))));

        given(productRepository.findByIdIn(Arrays.asList(토마토.getId(), 양상추.getId()))).willReturn(
                Arrays.asList(토마토, 양상추));

        // when, then
        assertThatThrownBy(
                () -> menuValidator.validate(빅맥)
        ).isInstanceOf(InvalidMenuPriceException.class);

    }

    @Test
    void 메뉴_상품_없음_검증() {
        // given
        Product 토마토 = ProductFactory.createProduct(1L, "토마토", 1000);
        Product 양상추 = ProductFactory.createProduct(2L, "양상추", 1000);
        Menu 빅맥 = new Menu(1L, MenuName.from("빅맥"), Price.from(3000), 1L,
                MenuProducts.from(Arrays.asList(new MenuProduct(토마토.getId(), Quantity.from(1)),
                        new MenuProduct(양상추.getId(), Quantity.from(5)))));

        given(productRepository.findByIdIn(Arrays.asList(토마토.getId(), 양상추.getId()))).willReturn(
                Arrays.asList(토마토));

        // when, then
        assertThatThrownBy(
                () -> menuValidator.validate(빅맥)
        ).isInstanceOf(NotFoundProductException.class);
    }
}