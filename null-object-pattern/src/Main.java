import factory.ShippingStrategyFactory;
import factory.ShippingType;
import model.Order;
import model.Product;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1");
        order.addProduct(new Product("Keyboard", new BigDecimal("50.00")));
        order.addProduct(new Product("Mouse", new BigDecimal("30.00")));

        // 1. Normal Strategy via Factory: Express ($15.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
        System.out.println("Factory (EXPRESS):        " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Null Object via Factory: passing null safely returns Null Object ($0.00, no NullPointerException)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(null));
        System.out.println("Factory (null -> NullObj): " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
