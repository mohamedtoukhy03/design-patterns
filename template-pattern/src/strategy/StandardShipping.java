package strategy;

import model.Order;
import java.math.BigDecimal;

public class StandardShipping implements ShippingStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        if (order.getItemsTotal().compareTo(new BigDecimal("100.00")) >= 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal("5.00");
    }
}
