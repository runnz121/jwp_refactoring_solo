package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.infra.ProductRepository;
import kitchenpos.menu.request.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        return productRepository.save(productRequest.toProduct());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}