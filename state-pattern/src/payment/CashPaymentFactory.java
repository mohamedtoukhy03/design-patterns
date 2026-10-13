package payment;

public class CashPaymentFactory extends PaymentProcessorFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new CashPaymentProcessor();
    }
}

