package state;

import model.Order;

/**
 * State when the parcel is in transit with the courier.
 */
public class ShippedOrderState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("❌ Order " + order.getId() + " is already paid.");
    }

    @Override
    public void ship(Order order) {
        System.out.println("❌ Order " + order.getId() + " has already been shipped.");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Order " + order.getId() + " delivered successfully. Transitioning: SHIPPED -> DELIVERED.");
        order.setState(new DeliveredOrderState());
    }

    @Override
    public void cancel(Order order) {
        System.out.println("❌ Cannot cancel order " + order.getId() + ": Package is already in transit. Return must be requested after delivery.");
    }

    @Override
    public String getName() {
        return "SHIPPED";
    }
}

