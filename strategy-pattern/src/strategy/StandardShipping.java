package strategy;

import model.Order;
import java.math.BigDecimal;

public class StandardShipping implements ShippingStrategy {

    private static final BigDecimal FLAT_FEE = new BigDecimal("5.00");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("100.00");

    @Override
    public BigDecimal calculate(Order order) {
        if (order.getItemsTotal().compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return FLAT_FEE;
    }
}
