package strategy;

import java.math.BigDecimal;
import model.Order;

public class ShippingStrategyNullObject implements ShippingStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        return BigDecimal.ZERO;
    }
}
