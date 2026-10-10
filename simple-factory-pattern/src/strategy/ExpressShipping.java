package strategy;

import model.Order;
import java.math.BigDecimal;

public class ExpressShipping implements ShippingStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        return new BigDecimal("15.00");
    }
}
