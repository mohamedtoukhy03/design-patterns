import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * International shipping strategy:
 * - Base cross-border documentation fee: $20.00
 * - International air-freight rate: $4.00 per kg
 * <p>
 * Demonstrates a weight-driven algorithm that scales linearly with {@code order.getWeight()}.
 */
public class InternationalShipping implements ShippingStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("20.00");
    private static final BigDecimal RATE_PER_KG = new BigDecimal("4.00");

    @Override
    public BigDecimal calculate(Order order) {
        BigDecimal weight = order.getWeight();
        BigDecimal shippingCost = BASE_FEE.add(RATE_PER_KG.multiply(weight));
        return shippingCost.setScale(2, RoundingMode.HALF_UP);
    }
}
