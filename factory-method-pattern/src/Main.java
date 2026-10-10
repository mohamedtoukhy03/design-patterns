import factory.ShippingStrategyFactory;
import factory.ShippingType;
import model.Order;
import model.Product;
import payment.CreditCardPaymentFactory;
import payment.PaymentProcessorFactory;
import payment.PayPalPaymentFactory;
import checkout.CheckoutService;
import checkout.InStoreCheckoutService;
import checkout.OnlineCheckoutService;
import checkout.PayPalCheckoutService;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // Create sample order
        Order order = new Order("ORD-202");
        order.addProduct(new Product("Mechanical Keyboard", new BigDecimal("70.00")));
        order.addProduct(new Product("Gaming Mouse",        new BigDecimal("30.00")));
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));

        System.out.println("==================================================");
        System.out.println("  1. STANDALONE FACTORY METHOD (Payment Factories)");
        System.out.println("==================================================");
        // Client uses the abstract creator and lets concrete factories decide which processor to create:
        PaymentProcessorFactory cardFactory = new CreditCardPaymentFactory();
        cardFactory.processPayment(order.getId(), order.getTotal());

        PaymentProcessorFactory paypalFactory = new PayPalPaymentFactory();
        paypalFactory.processPayment(order.getId(), order.getTotal());

        System.out.println();
        System.out.println("==================================================");
        System.out.println("  2. CHECKOUT PIPELINE FACTORY METHOD");
        System.out.println("==================================================");
        // Subclasses of CheckoutService override createPaymentProcessor() to supply their own payment type:

        // A. Online Checkout (creates & uses CreditCardPaymentProcessor)
        System.out.println("--- Online Credit Card Checkout ---");
        CheckoutService onlineCheckout = new OnlineCheckoutService();
        onlineCheckout.processOrder(order);

        // B. PayPal Checkout (creates & uses PayPalPaymentProcessor)
        System.out.println("--- Online PayPal Checkout ---");
        CheckoutService paypalCheckout = new PayPalCheckoutService();
        paypalCheckout.processOrder(order);

        // C. In-Store Checkout (creates & uses CashPaymentProcessor)
        System.out.println("--- In-Store POS Cash Checkout ---");
        Order inStoreOrder = new Order("ORD-STORE-5");
        inStoreOrder.addProduct(new Product("USB-C Cable", new BigDecimal("15.00")));
        inStoreOrder.setShippingStrategy(ShippingStrategyFactory.createStrategy(null)); // No shipping

        CheckoutService inStoreCheckout = new InStoreCheckoutService();
        inStoreCheckout.processOrder(inStoreOrder);
    }
}
