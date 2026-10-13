package payment;

import java.math.BigDecimal;

public class CashPaymentProcessor implements PaymentProcessor {

    @Override
    public void pay(String orderId, BigDecimal amount) {
        System.out.println("Processing in-store cash payment of $" + amount + " for order " + orderId);
    }
}

