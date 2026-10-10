package payment;

import java.math.BigDecimal;

public class CreditCardPaymentProcessor implements PaymentProcessor {

    @Override
    public void pay(String orderId, BigDecimal amount) {
        System.out.println("Processing credit card payment of $" + amount + " for order " + orderId);
    }
}

