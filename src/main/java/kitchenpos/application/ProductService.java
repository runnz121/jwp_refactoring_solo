package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRequest.toProduct();
        Product saveProduct = saveProduct(product);

        return ProductResponse.of(saveProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = findProducts();

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    private List<Product> findProducts() {
        return productRepository.findAll();
    }
}
