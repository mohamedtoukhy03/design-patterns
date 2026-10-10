package service;

import model.Order;

/**
 * Abstract class defining the Template Method for order checkout processing.
 */
public abstract class CheckoutService {

    // The Template Method: defines the invariant skeleton of the checkout workflow
    public final void processOrder(Order order) {
        validateOrder(order);
        calculateTotal(order);
        processPayment(order);
        dispatchOrder(order);
        printReceipt(order);
    }

    protected void validateOrder(Order order) {
        System.out.println("Validating order " + order.getId() + " (" + order.getProducts().size() + " items)...");
    }

    protected void calculateTotal(Order order) {
        System.out.println("Subtotal: $" + order.getItemsTotal() + " | Shipping: $" + order.getShippingCost() + " | Total: $" + order.getTotal());
    }

    // =========================================================================
    // PRIMITIVE (ABSTRACT) STEPS:
    // The template method defines the overall flow and WHEN these steps run,
    // but defers HOW they are executed to each subclass responsible for it.
    // =========================================================================
    protected abstract void processPayment(Order order);
    protected abstract void dispatchOrder(Order order);

    protected void printReceipt(Order order) {
        System.out.println("Order " + order.getId() + " completed successfully.\n");
    }
}
