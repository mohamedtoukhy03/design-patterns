package model;

import strategy.ShippingStrategy;
import strategy.StandardShipping;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String id;
    private final List<Product> products = new ArrayList<>();
    private ShippingStrategy shippingStrategy = new StandardShipping();

    public Order(String id) {
        this.id = id;
    }

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