package checkout;

import model.Order;
import payment.CashPaymentProcessor;
import payment.PaymentProcessor;

public class InStoreCheckoutService extends CheckoutService {

    // Overrides Factory Method: in-store orders instantiate Cash processor
    @Override
    protected PaymentProcessor createPaymentProcessor() {
        return new CashPaymentProcessor();
    }

    @Override
    protected void dispatchOrder(Order order) {
        System.out.println("Packing items into store shopping bag and handing to customer at register.");
    }
}

