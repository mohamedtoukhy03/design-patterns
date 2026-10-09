package strategy;

import model.Order;
import java.math.BigDecimal;

public class InternationalShipping implements ShippingStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("20.00");
    private static final BigDecimal RATE_PER_KG = new BigDecimal("4.00");

    @Override
    public BigDecimal calculate(Order order) {
        return BASE_FEE.add(RATE_PER_KG.multiply(order.getWeight()));
    }
}
