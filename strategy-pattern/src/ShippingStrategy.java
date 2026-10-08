import java.math.BigDecimal;

/**
 * Strategy interface defining the contract for shipping cost calculation.
 * <p>
 * Receives the entire {@link Order} so concrete strategies can access
 * whatever order details they need (e.g. weight, items total, destination)
 * without altering this interface.
 */
public interface ShippingStrategy {
    BigDecimal calculate(Order order);
}
