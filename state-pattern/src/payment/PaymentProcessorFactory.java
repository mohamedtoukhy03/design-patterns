package payment;

import java.math.BigDecimal;

/**
 * Creator base class defining the Factory Method.
 * <p>
 * Subclasses decide which {@link PaymentProcessor} to instantiate.
 */
public abstract class PaymentProcessorFactory {

    // =========================================================================
    // THE FACTORY METHOD:
    // Declared abstract so that each subclass instantiates the appropriate product.
    // =========================================================================
    public abstract PaymentProcessor createProcessor();

    // High-level operation that relies on the product from the factory method:
    public void processPayment(String orderId, BigDecimal amount) {
        PaymentProcessor processor = createProcessor();
        processor.pay(orderId, amount);
    }
}

