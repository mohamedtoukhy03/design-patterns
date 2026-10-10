# 📋 Template Method Pattern

> **Category:** Behavioral Design Pattern

---

## 1. What is the Template Method Pattern?

The **Template Method Pattern** defines the skeleton of an algorithm or workflow in a base class, but lets subclasses override specific steps of the algorithm without changing its overall structure.

### The Problem It Solves
When building multi-channel applications (such as an e-commerce platform processing orders from an online website versus a physical brick-and-mortar cash register):
* The overall order processing workflow is largely identical across channels:
  1. Validate the order
  2. Calculate total and shipping
  3. Process payment
  4. Dispatch or deliver items
  5. Print confirmation receipt
* However, specific steps differ significantly:
  * **Online:** Charges credit cards via gateway, ships packages via courier.
  * **In-Store:** Accepts register cash/card, hands bagged goods directly to the customer across the counter.
* Without the Template Method pattern, developers either copy-paste the entire multi-step workflow into separate classes (causing massive code duplication) or clutter a single method with fragile `if/else` checks for every channel.

---

## 2. The Code Example (Order Checkout Pipeline)

In this project, [`CheckoutService`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/checkout/CheckoutService.java) defines the immutable template method `processOrder(Order)`, while subclasses customize payment and dispatch steps.

```text
               +--------------------------------------+
               |      <<abstract>> CheckoutService    |
               +--------------------------------------+
               | + processOrder(Order): void  (final) |-----> Template Method
               | # validateOrder(Order): void         |
               | # calculateTotal(Order): void        |
               | # processPayment(Order): void (abs)  |
               | # dispatchOrder(Order): void  (abs)  |
               | # printReceipt(Order): void          |
               +--------------------------------------+
                                   ^
                                   |
                  +----------------+----------------+
                  |                                 |
+------------------------------------+   +------------------------------------+
|       OnlineCheckoutService        |   |       InStoreCheckoutService       |
+------------------------------------+   +------------------------------------+
| - Charges card via gateway         |   | - Charges cash/POS at register     |
| - Dispatches package with courier  |   | - Hands physical bag to customer   |
+------------------------------------+   +------------------------------------+
```

### Components:

1. **Abstract Base ([`CheckoutService.java`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/checkout/CheckoutService.java))**:
   The `processOrder` method is declared `final` so subclasses cannot alter the sequence of execution:
   ```java
   public abstract class CheckoutService {

       // The Template Method
       public final void processOrder(Order order) {
           validateOrder(order);
           calculateTotal(order);
           processPayment(order);
           dispatchOrder(order);
           printReceipt(order);
       }

       protected void validateOrder(Order order) {
           System.out.println("Validating order " + order.getId() + " (" + order.getProducts().size() + " items)...");
       }

       protected void calculateTotal(Order order) {
           System.out.println("Subtotal: $" + order.getItemsTotal() + " | Shipping: $" + order.getShippingCost() + " | Total: $" + order.getTotal());
       }

       // Primitive steps customized by concrete subclasses
       protected abstract void processPayment(Order order);
       protected abstract void dispatchOrder(Order order);

       protected void printReceipt(Order order) {
           System.out.println("Order " + order.getId() + " completed successfully.\n");
       }
   }
   ```

2. **Concrete Subclasses ([`checkout/`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/checkout/))**:
   * **`OnlineCheckoutService`**: Overrides `processPayment` by delegating to a [`PaymentProcessor`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/payment/PaymentProcessor.java) interface (the same target contract used in the Adapter Pattern!) and `dispatchOrder` (courier shipping).
   * **`InStoreCheckoutService`**: Overrides `processPayment` (POS register) and `dispatchOrder` (counter handoff).

3. **Payment Target Contract ([`PaymentProcessor.java`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/payment/PaymentProcessor.java))**:
   Matches the contract from the Adapter Pattern (`pay(orderId, amount)`), demonstrating how design patterns collaborate seamlessly without tight coupling.

4. **Domain Models ([`model/`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/model/))**:
   * **[`Product.java`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/model/Product.java)**: Represents individual purchased items (`name`, `price`).
   * **[`Order.java`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/model/Order.java)**: Holds items and calculates sums dynamically.

---

## 3. How the Example Reflects the Design Pattern

| Role | Class in Project | Explanation |
| :--- | :--- | :--- |
| **Abstract Class** | `CheckoutService` | Defines the template method `processOrder(...)` and invariant helper steps. |
| **Template Method** | `processOrder(Order)` | The `final` method that orchestrates the execution order of all steps. |
| **Primitive Operations** | `processPayment`, `dispatchOrder` | Abstract methods that subclasses are required to implement. |
| **Concrete Classes** | `OnlineCheckoutService`, `InStoreCheckoutService` | Provide specific implementations for the primitive operations. |
| **Adapter Synergy** | `PaymentProcessor` | Connects the Adapter Pattern's payment gateway into the Template Method pipeline. |

### Inversion of Control ("The Hollywood Principle"):
Notice that the base class calls the methods of the subclass, not the other way around: *"Don't call us, we'll call you."*

---

## 4. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/Main.java))
```java
public class Main {
    public static void main(String[] args) {
        // 1. Online Order Processing (Default Processor)
        Order onlineOrder = new Order("ORD-ONLINE-1");
        onlineOrder.addProduct(new Product("Mechanical Keyboard", new BigDecimal("70.00")));
        onlineOrder.addProduct(new Product("Gaming Mouse",        new BigDecimal("30.00")));
        onlineOrder.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));

        CheckoutService onlineCheckout = new OnlineCheckoutService();
        onlineCheckout.processOrder(onlineOrder);

        // 2. Online Order with Plugged-in Payment Adapter
        CheckoutService adapterCheckout = new OnlineCheckoutService((orderId, amount) -> {
            System.out.println("[Payment Gateway Adapter] Authorized $" + amount + " for " + orderId);
        });
        adapterCheckout.processOrder(onlineOrder);

        // 3. In-Store Order Processing
        Order inStoreOrder = new Order("ORD-STORE-2");
        inStoreOrder.addProduct(new Product("USB-C Cable", new BigDecimal("15.00")));
        inStoreOrder.setShippingStrategy(ShippingStrategyFactory.createStrategy(null)); // No shipping

        CheckoutService inStoreCheckout = new InStoreCheckoutService();
        inStoreCheckout.processOrder(inStoreOrder);
    }
}
```

### How to Compile & Run
```bash
cd template-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/payment/*.java src/checkout/*.java src/*.java
java -cp src Main
```

---

## 5. When to Use the Template Method Pattern

Use the Template Method Pattern when:
1. **Multiple classes share the same algorithm structure:** You have multiple variations of a multi-step workflow with only minor differences in individual steps.
2. **You want to prevent clients from altering the execution order:** Making the template method `final` guarantees that mandatory steps (e.g. security validation, payment verification, auditing) cannot be skipped or reordered.
3. **Refactoring duplicate code:** Common code across subclasses can be pulled up into a single location in the abstract base class.

---

## 6. Other Real-World Use Cases

* **Java Standard Library (JDK):**
  * `java.io.InputStream`: `read(byte[] b, int off, int len)` is a template method that repeatedly calls the abstract single-byte `read()`.
  * `java.util.AbstractList` / `AbstractMap`: Provides default implementations for collection methods (like `addAll()`) based on primitive methods like `get()` and `size()`.
* **Spring Framework:**
  * `JdbcTemplate`: Manages opening connections, starting transactions, cleaning up resources, and error mapping in a template method, deferring SQL query mapping to user callbacks (`RowMapper`).
* **Servlet API:**
  * `HttpServlet.service()`: A template method that parses HTTP requests and routes to `doGet()`, `doPost()`, or `doPut()` implemented in custom servlets.
* **Document Processing & Data Ingestion Pipelines:**
  * An `ETLPipeline` template defining `readSource()`, `transformData()`, `validateSchema()`, and `writeSink()`, with subclasses for CSV, JSON, and Parquet data pipelines.
