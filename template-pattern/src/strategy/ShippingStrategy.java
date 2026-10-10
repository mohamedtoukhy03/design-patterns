package strategy;

import model.Order;
import java.math.BigDecimal;

/**
 * Strategy interface defining the contract for shipping cost calculation.
 */
public interface ShippingStrategy {
    BigDecimal calculate(Order order);
}

