import java.math.BigDecimal;

public interface PaymentProcessor {
    void pay(String orderId, BigDecimal amount);
}

