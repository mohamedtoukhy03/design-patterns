package service;

import java.math.BigDecimal;

/**
 * Target payment interface (matches the contract from the Adapter Pattern).
 * Allows plugging in different payment adapters (e.g. Stripe, PayPal, LegacyBank).
 */
public interface PaymentProcessor {
    void pay(String orderId, BigDecimal amount);
}

