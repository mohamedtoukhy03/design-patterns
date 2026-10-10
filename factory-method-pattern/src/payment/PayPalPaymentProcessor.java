package payment;

import java.math.BigDecimal;

public class PayPalPaymentProcessor implements PaymentProcessor {

    @Override
    public void pay(String orderId, BigDecimal amount) {
        System.out.println("Processing PayPal payment of $" + amount + " for order " + orderId);
    }
}

