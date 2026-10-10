import model.Order;
import model.Product;
import strategy.ExpressShipping;
import strategy.InternationalShipping;
import strategy.NextDayShipping;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1");
        order.addProduct(new Product("Keyboard", new BigDecimal("50.00")));
        order.addProduct(new Product("Mouse", new BigDecimal("30.00")));

        // 1. Default strategy: Standard Shipping ($5.00)
        System.out.println("Standard Shipping:      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Swap to Express Shipping ($15.00)
        order.setShippingStrategy(new ExpressShipping());
        System.out.println("Express Shipping:       " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 3. Swap to International Shipping ($30.00)
        order.setShippingStrategy(new InternationalShipping());
        System.out.println("International Shipping: " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 4. Swap to NextDay Shipping ($25.00)
        order.setShippingStrategy(new NextDayShipping());
        System.out.println("NextDay Shipping:       " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
