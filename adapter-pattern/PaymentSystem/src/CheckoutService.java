import java.math.BigDecimal;
import java.util.Objects;

public class CheckoutService {
    private final PaymentProcessor processor;

    public CheckoutService(PaymentProcessor processor) {
        this.processor = Objects.requireNonNull(processor, "processor must not be null");
    }

    public void checkout(String orderId, BigDecimal total) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be null or blank");
        }
        if (total == null || total.signum() <= 0) {
            throw new IllegalArgumentException("total must be greater than zero");
        }
        processor.pay(orderId, total);
    }
}

