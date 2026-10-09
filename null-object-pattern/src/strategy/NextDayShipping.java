package strategy;

import model.Order;
import java.math.BigDecimal;

public class NextDayShipping implements ShippingStrategy {

    private static final BigDecimal FLAT_FEE = new BigDecimal("25.00");
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("200.00");
    private static final BigDecimal INSURANCE_FEE = new BigDecimal("5.00");

    @Override
    public BigDecimal calculate(Order order) {
        BigDecimal fee = FLAT_FEE;

        if (order.getItemsTotal().compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            fee = fee.add(INSURANCE_FEE);
        }

        return fee;
    }
}
