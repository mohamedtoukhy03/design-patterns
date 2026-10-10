import factory.ShippingStrategyFactory;
import factory.ShippingType;
import model.Order;
import model.Product;
import service.CheckoutService;
import service.InStoreCheckoutService;
import service.OnlineCheckoutService;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // 1. Online order with shipping
        Order onlineOrder = new Order("ORD-ONLINE-1");
        onlineOrder.addProduct(new Product("Mechanical Keyboard", new BigDecimal("70.00")));
        onlineOrder.addProduct(new Product("Gaming Mouse",        new BigDecimal("30.00")));
        onlineOrder.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));

        System.out.println("=== 1. Online Order (Default Payment Processor) ===");
        CheckoutService onlineCheckout = new OnlineCheckoutService();
        onlineCheckout.processOrder(onlineOrder);

        // Demonstrating how an Adapter / Custom Gateway plugs into the Template Method
        System.out.println("=== 2. Online Order (Plugged-In Payment Adapter) ===");
        CheckoutService adapterCheckout = new OnlineCheckoutService((orderId, amount) -> {
            System.out.println("[Payment Gateway Adapter] Authorized transaction of $" + amount + " for " + orderId);
        });
        adapterCheckout.processOrder(onlineOrder);

        // 2. In-store order (no shipping)
        Order inStoreOrder = new Order("ORD-STORE-2");
        inStoreOrder.addProduct(new Product("USB-C Cable", new BigDecimal("15.00")));
        inStoreOrder.setShippingStrategy(ShippingStrategyFactory.createStrategy(null));

        System.out.println("=== 3. In-Store Order (Register POS) ===");
        CheckoutService inStoreCheckout = new InStoreCheckoutService();
        inStoreCheckout.processOrder(inStoreOrder);
    }
}
