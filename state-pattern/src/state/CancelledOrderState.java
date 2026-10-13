package state;

import model.Order;

/**
 * Terminal failure state: order was cancelled.
 */
public class CancelledOrderState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("❌ Cannot pay for order " + order.getId() + ": Order is cancelled.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("❌ Cannot ship order " + order.getId() + ": Order is cancelled.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("❌ Cannot deliver order " + order.getId() + ": Order is cancelled.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("❌ Order " + order.getId() + " is already cancelled.");
    }

    @Override
    public String getName() {
        return "CANCELLED";
    }
}

