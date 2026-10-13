package model;

import state.OrderState;
import state.PendingOrderState;
import strategy.ShippingStrategy;
import strategy.StandardShipping;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Context class in the State Pattern.
 * <p>
 * Maintains a reference to an {@link OrderState} instance that represents the
 * current status of the order, and delegates state-specific behavior to it.
 */
public class Order {
    private final String id;
    private final List<Product> products = new ArrayList<>();
    private ShippingStrategy shippingStrategy = new StandardShipping();
    private OrderState state;

    public Order(String id) {
        this.id = id;
        this.state = new PendingOrderState(); // Initial state
    }

    // =========================================================================
    // STATE PATTERN DELEGATION:
    // Order delegates actions to the current OrderState object.
    // =========================================================================

    public void pay() {
        state.pay(this);
    }

    public void ship() {
        state.ship(this);
    }

    public void deliver() {
        state.deliver(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public String getStatusName() {
        return state.getName();
    }

    // =========================================================================
    // PRODUCT & SHIPPING LOGIC:
    // =========================================================================

    public void addProduct(Product product) {
        products.add(product);
    }

    public BigDecimal getItemsTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Product p : products) {
            total = total.add(p.getPrice());
        }
        return total;
    }

    public BigDecimal getShippingCost() {
        return shippingStrategy.calculate(this);
    }

    public BigDecimal getTotal() {
        return getItemsTotal().add(getShippingCost());
    }

    public void setShippingStrategy(ShippingStrategy shippingStrategy) {
        this.shippingStrategy = shippingStrategy;
    }

    public String getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public ShippingStrategy getShippingStrategy() {
        return shippingStrategy;
    }
}
