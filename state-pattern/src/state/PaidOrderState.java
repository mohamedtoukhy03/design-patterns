package state;

import model.Order;

/**
 * State when payment has been completed and the order is being prepared for dispatch.
 */
public class PaidOrderState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("❌ Order " + order.getId() + " is already paid.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Order " + order.getId() + " has been shipped. Transitioning: PAID -> SHIPPED.");
        order.setState(new ShippedOrderState());
    }

    @Override
    public void deliver(Order order) {
        System.out.println("❌ Cannot deliver order " + order.getId() + ": Order has not been shipped yet.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Order " + order.getId() + " cancelled. Refund issued to customer. Transitioning: PAID -> CANCELLED.");
        order.setState(new CancelledOrderState());
    }

    @Override
    public String getName() {
        return "PAID";
    }
}

