package checkout;

import model.Order;
import payment.PaymentProcessor;

/**
 * Creator class that also coordinates order checkout.
 * <p>
 * Contains the Factory Method {@link #createPaymentProcessor()} which subclasses
 * override to provide the right payment processor for their specific channel.
 */
public abstract class CheckoutService {

    // =========================================================================
    // THE FACTORY METHOD:
    // Subclasses decide WHICH concrete PaymentProcessor to instantiate.
    // =========================================================================
    protected abstract PaymentProcessor createPaymentProcessor();

    // Template workflow executing the order:
    public final void processOrder(Order order) {
        validateOrder(order);
        calculateTotal(order);

        // Uses the product created by the Factory Method:
        PaymentProcessor processor = createPaymentProcessor();
        processor.pay(order.getId(), order.getTotal());

        dispatchOrder(order);
        printReceipt(order);
    }

    protected void validateOrder(Order order) {
        System.out.println("Validating order " + order.getId() + " (" + order.getProducts().size() + " items)...");
    }

    protected void calculateTotal(Order order) {
        System.out.println("Subtotal: $" + order.getItemsTotal() + " | Shipping: $" + order.getShippingCost() + " | Total: $" + order.getTotal());
    }

    protected abstract void dispatchOrder(Order order);

    protected void printReceipt(Order order) {
        System.out.println("Order " + order.getId() + " completed successfully.\n");
    }
}

