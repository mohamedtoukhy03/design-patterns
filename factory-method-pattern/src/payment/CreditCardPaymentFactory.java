package payment;

public class CreditCardPaymentFactory extends PaymentProcessorFactory {

    @Override
    public PaymentProcessor createProcessor() {
        return new CreditCardPaymentProcessor();
    }
}

