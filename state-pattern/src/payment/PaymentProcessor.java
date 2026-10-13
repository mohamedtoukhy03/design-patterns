package payment;

import java.math.BigDecimal;

/**
 * Product interface for all payment processors.
 */
public interface PaymentProcessor {
    void pay(String orderId, BigDecimal amount);
}

