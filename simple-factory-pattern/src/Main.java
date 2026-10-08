import factory.ShippingStrategyFactory;
import factory.ShippingType;
import model.Order;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1", new BigDecimal("80.00"), new BigDecimal("2.5"));

        // 1. Default Strategy: Standard Shipping ($5.00)
        System.out.println("Default (Standard):      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Select Express via Factory
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
        System.out.println("Factory (EXPRESS):       " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 3. Select International via Factory
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.INTERNATIONAL));
        System.out.println("Factory (INTERNATIONAL): " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 4. Select NextDay via Factory
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.NEXT_DAY));
        System.out.println("Factory (NEXT_DAY):      " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
