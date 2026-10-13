package state;

import model.Order;

/**
 * Terminal successful state: customer has received the package.
 */
public class DeliveredOrderState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("❌ Order " + order.getId() + " is already delivered and complete.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("❌ Order " + order.getId() + " has already been delivered.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("❌ Order " + order.getId() + " is already delivered.");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("❌ Cannot cancel order " + order.getId() + ": Order has already been delivered. Please initiate a return request.");
    }

    @Override
    public String getName() {
        return "DELIVERED";
    }
}

