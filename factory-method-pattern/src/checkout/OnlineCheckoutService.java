package checkout;

import model.Order;
import payment.CreditCardPaymentProcessor;
import payment.PaymentProcessor;

public class OnlineCheckoutService extends CheckoutService {

    // Overrides Factory Method: online orders use Credit Card by default
    @Override
    protected PaymentProcessor createPaymentProcessor() {
        return new CreditCardPaymentProcessor();
    }

    @Override
    protected void dispatchOrder(Order order) {
        System.out.println("Dispatching parcel via " + order.getShippingStrategy().getClass().getSimpleName() + " (Fee: $" + order.getShippingCost() + ")");
    }
}

