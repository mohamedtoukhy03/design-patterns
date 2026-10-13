package state;

import model.Order;

/**
 * Initial state when an order is first placed and awaits payment.
 */
public class PendingOrderState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("Payment processed for order " + order.getId() + ". Transitioning: PENDING -> PAID.");
        order.setState(new PaidOrderState());
    }

    @Override
    public void ship(Order order) {
        System.out.println("❌ Cannot ship order " + order.getId() + ": Order is pending payment.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("❌ Cannot deliver order " + order.getId() + ": Order is pending payment.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Order " + order.getId() + " has been cancelled. Transitioning: PENDING -> CANCELLED.");
        order.setState(new CancelledOrderState());
    }

    @Override
    public String getName() {
        return "PENDING";
    }
}

