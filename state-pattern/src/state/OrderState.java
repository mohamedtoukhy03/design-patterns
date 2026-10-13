package state;

import model.Order;

/**
 * State interface defining the actions that can be performed on an Order.
 * <p>
 * Concrete states determine how each action behaves and handle valid
 * transitions to subsequent states.
 */
public interface OrderState {

    void pay(Order order);

    void ship(Order order);

    void deliver(Order order);

    void cancel(Order order);

    String getName();
}

