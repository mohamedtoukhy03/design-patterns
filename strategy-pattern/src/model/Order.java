package model;

import strategy.ShippingStrategy;
import strategy.StandardShipping;
import java.math.BigDecimal;

/**
 * Order context holding details and delegating shipping calculation to the active strategy.
 */
public class Order {
    private final String id;
    private final BigDecimal itemsTotal;
    private final BigDecimal weight;
    private ShippingStrategy shippingStrategy;

    public Order(String id, BigDecimal itemsTotal, BigDecimal weight) {
        this.id = id;
        this.itemsTotal = itemsTotal;
        this.weight = weight;
        this.shippingStrategy = new StandardShipping(); // Default strategy
    }

    public void setShippingStrategy(ShippingStrategy shippingStrategy) {
        this.shippingStrategy = shippingStrategy;
    }

    public BigDecimal getShippingCost() {
        return shippingStrategy.calculate(this);
    }

    public BigDecimal getTotal() {
        return itemsTotal.add(getShippingCost());
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