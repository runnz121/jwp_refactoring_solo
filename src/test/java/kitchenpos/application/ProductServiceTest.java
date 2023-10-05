package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.product.repository.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;


    @Test
    @DisplayName("상품이 생성되는지 확인하는 테스트")
    void createTest() {

        Product givenProduct = new Product();
        BigDecimal givenPrice = BigDecimal.valueOf(1000);
        givenProduct.setPrice(givenPrice);

        // when
        when(productDao.save(any(Product.class)))
            .thenReturn(givenProduct);

        Product actual = productService.create(givenProduct);
        assertThat(actual).isEqualTo(givenProduct);
    }

    @Test
    @DisplayName("가격이 없는 상품일 경우 예외를 발생 시키는지 확인하는 테스트")
    void throwExceptionTestWithoutPrice() {

        Product givenProduct = new Product();

        assertThatThrownBy(() -> productService.create(givenProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 0보다 낮은  상품일 경우 예외를 발생 시키는지 확인하는 테스트")
    void throwExceptionTestUnderZeroPrice() {

        Product givenProduct = new Product();
        BigDecimal underZero = BigDecimal.valueOf(-1);
        givenProduct.setPrice(underZero);

        assertThatThrownBy(() -> productService.create(givenProduct))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("리스트를 반환하는지 확인하는 테스트")
    void listTest() {

        final Product givenProduct1 = new Product();
        final Product givenProduct2 = new Product();

        // when
        when(productDao.findAll())
            .thenReturn(Arrays.asList(givenProduct1, givenProduct2));

        List<Product> actual = productService.list();

        assertThat(actual)
            .containsExactly(givenProduct1, givenProduct2);
    }
}