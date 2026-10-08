import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Next-Day priority shipping strategy:
 * - Base priority overnight fee: $25.00
 * - High-value insurance: Adds $5.00 signature confirmation & parcel insurance
 *   if the order total is $200.00 or higher.
 * <p>
 * Proves the Open/Closed Principle: added to the system without modifying
 * {@link Order} or any existing strategy class.
 */
public class NextDayShipping implements ShippingStrategy {

    private static final BigDecimal FLAT_FEE = new BigDecimal("25.00");
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("200.00");
    private static final BigDecimal INSURANCE_FEE = new BigDecimal("5.00");

    @Override
    public BigDecimal calculate(Order order) {
        BigDecimal fee = FLAT_FEE;

        // Add guaranteed signature delivery and transit insurance for high-value orders
        if (order.getItemsTotal().compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            fee = fee.add(INSURANCE_FEE);
        }

        return fee.setScale(2, RoundingMode.HALF_UP);
    }
}
