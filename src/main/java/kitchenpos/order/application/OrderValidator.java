package kitchenpos.order.application;

import kitchenpos.table.domain.TableGroup;

public interface OrderValidator {
	void validateUngroup(TableGroup tableGroup);
}
