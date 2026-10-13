import checkout.CheckoutService;
import checkout.OnlineCheckoutService;
import factory.ShippingStrategyFactory;
import factory.ShippingType;
import model.Order;
import model.Product;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  1. HAPPY PATH: PENDING -> PAID -> SHIPPED -> DELIVERED");
        System.out.println("==================================================");
        Order order1 = new Order("ORD-101");
        order1.addProduct(new Product("Mechanical Keyboard", new BigDecimal("70.00")));
        order1.addProduct(new Product("Gaming Mouse",        new BigDecimal("30.00")));
        order1.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));

        System.out.println("Initial status: " + order1.getStatusName());
        order1.pay();      // PENDING -> PAID
        System.out.println("Current status: " + order1.getStatusName());
        order1.ship();     // PAID -> SHIPPED
        System.out.println("Current status: " + order1.getStatusName());
        order1.deliver();  // SHIPPED -> DELIVERED
        System.out.println("Final status:   " + order1.getStatusName());

        System.out.println();
        System.out.println("==================================================");
        System.out.println("  2. CANCELLATION PATH: PENDING -> CANCELLED");
        System.out.println("==================================================");
        Order order2 = new Order("ORD-102");
        order2.addProduct(new Product("USB-C Hub", new BigDecimal("25.00")));

        System.out.println("Initial status: " + order2.getStatusName());
        order2.cancel();   // PENDING -> CANCELLED
        System.out.println("Current status: " + order2.getStatusName());

        // Attempting an action on cancelled order:
        System.out.println("Attempting to pay for cancelled order:");
        order2.pay();      // Rejected by CancelledOrderState

        System.out.println();
        System.out.println("==================================================");
        System.out.println("  3. PREVENTING INVALID STATE TRANSITIONS");
        System.out.println("==================================================");
        Order order3 = new Order("ORD-103");
        order3.addProduct(new Product("Wireless Headphones", new BigDecimal("120.00")));

        // A. Cannot ship before payment
        System.out.println("[Test 1] Attempting to ship unpaid order:");
        order3.ship();

        // B. Pay and Ship successfully
        System.out.println("\n[Test 2] Paying and shipping:");
        order3.pay();
        order3.ship();

        // C. Cannot cancel while in transit
        System.out.println("\n[Test 3] Attempting to cancel package in transit:");
        order3.cancel();

        // D. Deliver successfully
        System.out.println("\n[Test 4] Delivering package:");
        order3.deliver();

        // E. Cannot cancel after delivery
        System.out.println("\n[Test 5] Attempting to cancel delivered order:");
        order3.cancel();

        System.out.println();
        System.out.println("==================================================");
        System.out.println("  4. INTEGRATED CHECKOUT PIPELINE WITH STATE");
        System.out.println("==================================================");
        Order order4 = new Order("ORD-104");
        order4.addProduct(new Product("4K Monitor", new BigDecimal("350.00")));
        order4.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));

        System.out.println("Order status before checkout: " + order4.getStatusName());
        CheckoutService onlineCheckout = new OnlineCheckoutService();
        onlineCheckout.processOrder(order4); // Automatically validates, pays, and ships!

        System.out.println("Order status after checkout:  " + order4.getStatusName());
        System.out.println("Courier completes delivery:");
        order4.deliver();
        System.out.println("Order status after delivery:  " + order4.getStatusName());
    }
}
