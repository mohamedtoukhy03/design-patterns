package service;

import model.Order;

public class OnlineCheckoutService extends CheckoutService {

    private final PaymentProcessor paymentProcessor;

    // Default processor (requires no external server)
    public OnlineCheckoutService() {
        this((orderId, amount) ->
            System.out.println("PaymentProcessor charged $" + amount + " for order " + orderId)
        );
    }

    // Allows injecting any PaymentProcessor (such as LegacyBankAdapter from the Adapter Pattern)
    public OnlineCheckoutService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    @Override
    protected void processPayment(Order order) {
        paymentProcessor.pay(order.getId(), order.getTotal());
    }

    @Override
    protected void dispatchOrder(Order order) {
        System.out.println("Dispatching parcel via " + order.getShippingStrategy().getClass().getSimpleName() + " (Fee: $" + order.getShippingCost() + ")");
    }
}
