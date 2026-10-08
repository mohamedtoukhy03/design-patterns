import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * The Context class in the Strategy Pattern.
 * <p>
 * Holds order details and delegates shipping calculation to the configured
 * {@link ShippingStrategy} without knowing the underlying pricing rules.
 */
public class Order {
    private final String id;
    private final BigDecimal itemsTotal;
    private final BigDecimal weight;
    private ShippingStrategy shippingStrategy;

    public Order(String id, BigDecimal itemsTotal, BigDecimal weight) {
        this.id = id;
        this.itemsTotal = itemsTotal.setScale(2, RoundingMode.HALF_UP);
        this.weight = weight;
        this.shippingStrategy = new StandardShipping();
    }

    /**
     * Changes the shipping strategy at runtime.
     *
     * @param shippingStrategy the new strategy to use, must not be null
     */
    public void setShippingStrategy(ShippingStrategy shippingStrategy) {
        this.shippingStrategy = Objects.requireNonNull(shippingStrategy, "Shipping strategy must not be null");
    }


    public BigDecimal getShippingCost() {
        return shippingStrategy.calculate(this);
    }


    public BigDecimal getTotal() {
        return itemsTotal.add(getShippingCost()).setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public BigDecimal getItemsTotal() {
        return itemsTotal;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public ShippingStrategy getShippingStrategy() {
        return shippingStrategy;
    }
}
