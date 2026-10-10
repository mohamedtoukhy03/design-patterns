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

        // 1. Default Strategy: Standard Shipping ($5.00)
        System.out.println("Default (Standard):      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Select Express via Factory ($15.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
        System.out.println("Factory (EXPRESS):       " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 3. Select International via Factory ($30.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.INTERNATIONAL));
        System.out.println("Factory (INTERNATIONAL): " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 4. Select NextDay via Factory ($25.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.NEXT_DAY));
        System.out.println("Factory (NEXT_DAY):      " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
