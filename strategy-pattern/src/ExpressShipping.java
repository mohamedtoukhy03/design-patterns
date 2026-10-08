import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Express shipping strategy:
 * - Base expedited fee: $15.00
 * - Overweight handling: Packages over 5.0 kg incur an extra $2.00 surcharge per extra kg.
 * <p>
 * Demonstrates how the strategy uses {@code order.getWeight()} for courier weight limits
 * while keeping the base fee constant for standard parcels.
 */
public class ExpressShipping implements ShippingStrategy {

    private static final BigDecimal BASE_FEE = new BigDecimal("15.00");
    private static final BigDecimal WEIGHT_THRESHOLD = new BigDecimal("5.00");
    private static final BigDecimal HEAVY_SURCHARGE_PER_KG = new BigDecimal("2.00");

    @Override
    public BigDecimal calculate(Order order) {
        BigDecimal fee = BASE_FEE;

        // Add expedited surcharge for parcels exceeding standard courier weight limit
        if (order.getWeight().compareTo(WEIGHT_THRESHOLD) > 0) {
            BigDecimal extraWeight = order.getWeight().subtract(WEIGHT_THRESHOLD);
            fee = fee.add(extraWeight.multiply(HEAVY_SURCHARGE_PER_KG));
        }

        return fee.setScale(2, RoundingMode.HALF_UP);
    }
}
