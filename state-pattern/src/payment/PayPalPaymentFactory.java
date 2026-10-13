package payment;

public class PayPalPaymentFactory extends PaymentProcessorFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new PayPalPaymentProcessor();
    }
}

