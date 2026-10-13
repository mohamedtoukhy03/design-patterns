package strategy;

import model.Order;
import java.math.BigDecimal;

public class NextDayShipping implements ShippingStrategy {

    @Override
    public BigDecimal calculate(Order order) {
        return new BigDecimal("25.00");
    }
}
