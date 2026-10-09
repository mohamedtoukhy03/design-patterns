package strategy;

import model.Order;
import java.math.BigDecimal;

public class ExpressShipping implements ShippingStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("15.00");
    private static final BigDecimal WEIGHT_THRESHOLD = new BigDecimal("5.00");
    private static final BigDecimal HEAVY_SURCHARGE_PER_KG = new BigDecimal("2.00");

    @Override
    public BigDecimal calculate(Order order) {
        BigDecimal fee = BASE_FEE;

        if (order.getWeight().compareTo(WEIGHT_THRESHOLD) > 0) {
            BigDecimal extraWeight = order.getWeight().subtract(WEIGHT_THRESHOLD);
            fee = fee.add(extraWeight.multiply(HEAVY_SURCHARGE_PER_KG));
        }

        return fee;
    }
}
