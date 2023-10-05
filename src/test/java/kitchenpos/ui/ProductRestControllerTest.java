package kitchenpos.ui;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.product.service.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.ProductRestController;

@WebMvcTest(ProductRestController.class)
@DisplayName("ProductRestControllerTest")
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Nested
    @DisplayName("/api/products")
    class Create {

        Product givenProduct = new Product();

        @BeforeEach
        void setUp() {
            givenProduct.setName("치킨");
            givenProduct.setPrice(BigDecimal.valueOf(17_000));

            when(productService.create(any(Product.class)))
                .thenReturn(givenProduct);
        }

        @DisplayName("201 과 함께 생성된 상품을 응답한다")
        @Test
        void responseWithProduct() throws Exception {
            mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(givenProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().string(
                    objectMapper.writeValueAsString(givenProduct)
                ));
        }

    }

    @Nested
    @DisplayName("/api/products")
    class GetList {

        @Nested
        @DisplayName("등록된 상품목록이 존재할 경우")
        class ReturnProductLists {

            List<Product> givenProducts;

            @BeforeEach
            void setUp() {
                Product product = new Product();
                givenProducts = Collections.singletonList(product);
                when(productService.list())
                    .thenReturn(givenProducts);
            }

            @DisplayName("200 과 함께 상품 리스트를 응답한다")
            @Test
            void responseWithProduct() throws Exception {
                mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(
                        objectMapper.writeValueAsString(givenProducts)
                    ));
            }
        }
    }
}