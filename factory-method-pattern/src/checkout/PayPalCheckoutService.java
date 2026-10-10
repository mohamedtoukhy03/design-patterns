package checkout;

import model.Order;
import payment.PayPalPaymentProcessor;
import payment.PaymentProcessor;

public class PayPalCheckoutService extends CheckoutService {

    // Overrides Factory Method: PayPal orders instantiate PayPal processor
    @Override
    protected PaymentProcessor createPaymentProcessor() {
        return new PayPalPaymentProcessor();
    }

    @Override
    protected void dispatchOrder(Order order) {
        System.out.println("Dispatching parcel via " + order.getShippingStrategy().getClass().getSimpleName() + " (Fee: $" + order.getShippingCost() + ")");
    }
}

