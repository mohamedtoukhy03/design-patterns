package service;

import model.Order;

public class InStoreCheckoutService extends CheckoutService {

    @Override
    protected void processPayment(Order order) {
        System.out.println("Processing in-store POS cash/card payment for $" + order.getTotal());
    }

    @Override
    protected void dispatchOrder(Order order) {
        System.out.println("Packing items into store shopping bag and handing to customer at register.");
    }
}

