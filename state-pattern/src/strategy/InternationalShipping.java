package strategy;

import model.Order;
import java.math.BigDecimal;

public class InternationalShipping implements ShippingStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        return new BigDecimal("30.00");
    }
}
