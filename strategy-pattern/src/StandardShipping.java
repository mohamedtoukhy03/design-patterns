import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Standard shipping strategy:
 * - Base flat fee: $5.00
 * - Promotion: FREE shipping ($0.00) if the order total is $100.00 or more.
 * <p>
 * Demonstrates why passing the entire {@link Order} is powerful:
 * the strategy inspects {@code order.getItemsTotal()} to apply promotional rules
 * without cluttering {@link Order} with promotion logic.
 */
public class StandardShipping implements ShippingStrategy {

    private static final BigDecimal FLAT_FEE = new BigDecimal("5.00");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("100.00");

    @Override
    public BigDecimal calculate(Order order) {
        // Free shipping for qualifying high-value orders
        if (order.getItemsTotal().compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return FLAT_FEE;
    }
}
