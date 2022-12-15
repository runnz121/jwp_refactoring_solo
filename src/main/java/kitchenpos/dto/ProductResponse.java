package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    private ProductResponse() {
    }

    private ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price.longValue(), that.price.longValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
