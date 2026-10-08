import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1", new BigDecimal("80.00"), new BigDecimal("2.5"));

        // 1. Default strategy: Standard Shipping ($5.00)
        System.out.println("Standard Shipping:      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Swap to Express Shipping ($15.00)
        order.setShippingStrategy(new ExpressShipping());
        System.out.println("Express Shipping:       " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 3. Swap to International Shipping ($20.00 base + 4.00 * 2.5 kg = $30.00)
        order.setShippingStrategy(new InternationalShipping());
        System.out.println("International Shipping: " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 4. Swap to NextDay Shipping ($25.00)
        order.setShippingStrategy(new NextDayShipping());
        System.out.println("NextDay Shipping:       " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
