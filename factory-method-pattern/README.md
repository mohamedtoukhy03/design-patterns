# 🏭 Factory Method Pattern

> **Category:** Creational Design Pattern (Gang of Four)

---

## 1. What is the Factory Method Pattern?

The **Factory Method Pattern** defines an interface or abstract method for creating an object, but **lets subclasses decide which class to instantiate**. It lets a class defer instantiation to its subclasses.

### The Problem It Solves
Consider our checkout system where customers can pay using Credit Card, PayPal, or In-Store Cash.

A naive approach uses a **Simple Factory** with conditional statements:
```java
// ❌ Problematic Simple Factory:
public class PaymentFactory {
    public static PaymentProcessor create(String type) {
        if ("CARD".equals(type))   return new CreditCardProcessor();
        if ("PAYPAL".equals(type)) return new PayPalProcessor();
        if ("CASH".equals(type))   return new CashProcessor();
        throw new IllegalArgumentException();
    }
}
```

**Why this breaks down as systems grow:**
1. **Violates Open/Closed Principle (OCP):** Every time you add a new payment method (e.g. Apple Pay, Crypto, Bank Transfer), you must open, modify, and re-test `PaymentFactory.java`.
2. **Tight Central Coupling:** A single factory class must know about every single payment provider in the universe.
3. **Inflexible Workflows:** A sales channel (like `OnlineCheckoutService`) cannot easily customize its own payment processor without passing configuration strings into a central switch statement.

---

## 2. Simple Factory vs. Factory Method: Key Differences

| Aspect | Simple Factory (Module 4) | Factory Method (This Module) |
| :--- | :--- | :--- |
| **Class Structure** | A single standalone class with a static method. | An inheritance hierarchy of Creator classes. |
| **Instantiation Logic** | Uses `switch` or `if/else` checks. | Uses **polymorphism and overriding**. |
| **Extensibility (OCP)** | Must modify the factory class to add new products. | **Add a new subclass** without touching any existing code! |
| **Object Creation** | The client calls `Factory.create(type)`. | Subclasses implement `createProduct()`. |

---

## 3. The Code Example (Payment & Checkout Pipeline)

```text
       [PaymentProcessor]  <--- Interface (Product)
               ^
     +---------+---------+
     |                   |
[CreditCardProcessor] [PayPalProcessor] [CashProcessor]
     ^                   ^
     | creates           | creates
[CreditCardFactory]  [PayPalFactory]    [CashFactory]
     \                   |             /
      +------------------+------------+
                         |
           [PaymentProcessorFactory] <--- Abstract Creator
                         |
           + createProcessor(): PaymentProcessor (FACTORY METHOD)
           + processPayment(orderId, amount): void
```

### Components:

1. **The Product Interface ([`payment/PaymentProcessor.java`](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/src/payment/PaymentProcessor.java))**:
   ```java
   public interface PaymentProcessor {
       void pay(String orderId, BigDecimal amount);
   }
   ```

2. **Concrete Products ([`payment/`](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/src/payment/))**:
   * `CreditCardPaymentProcessor`: Charges customer credit card.
   * `PayPalPaymentProcessor`: Authorizes payment via PayPal account.
   * `CashPaymentProcessor`: Accepts cash at the register counter.

3. **The Creator Base Class ([`payment/PaymentProcessorFactory.java`](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/src/payment/PaymentProcessorFactory.java))**:
   Declares the abstract **Factory Method**:
   ```java
   public abstract class PaymentProcessorFactory {

       // === THE FACTORY METHOD ===
       public abstract PaymentProcessor createProcessor();

       // Operation that uses the created product:
       public void processPayment(String orderId, BigDecimal amount) {
           PaymentProcessor processor = createProcessor(); // Polymorphic creation!
           processor.pay(orderId, amount);
       }
   }
   ```

4. **Concrete Creators ([`payment/`](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/src/payment/))**:
   Each subclass overrides `createProcessor()` to return its own product:
   ```java
   public class CreditCardPaymentFactory extends PaymentProcessorFactory {
       @Override
       public PaymentProcessor createProcessor() {
           return new CreditCardPaymentProcessor();
       }
   }

   public class PayPalPaymentFactory extends PaymentProcessorFactory {
       @Override
       public PaymentProcessor createProcessor() {
           return new PayPalPaymentProcessor();
       }
   }
   ```

5. **Integrated Workflow ([`checkout/CheckoutService.java`](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/src/checkout/CheckoutService.java))**:
   The Factory Method can also be embedded directly in a workflow template:
   ```java
   public abstract class CheckoutService {

       // Factory method deferred to subclasses:
       protected abstract PaymentProcessor createPaymentProcessor();

       public final void processOrder(Order order) {
           validateOrder(order);
           calculateTotal(order);

           // Obtains processor via Factory Method:
           PaymentProcessor processor = createPaymentProcessor();
           processor.pay(order.getId(), order.getTotal());

           dispatchOrder(order);
           printReceipt(order);
       }
   }
   ```
   * `OnlineCheckoutService`: overrides `createPaymentProcessor()` $\rightarrow$ returns `CreditCardPaymentProcessor`.
   * `PayPalCheckoutService`: overrides `createPaymentProcessor()` $\rightarrow$ returns `PayPalPaymentProcessor`.
   * `InStoreCheckoutService`: overrides `createPaymentProcessor()` $\rightarrow$ returns `CashPaymentProcessor`.

---

## 4. How the Example Reflects the Design Pattern

| Role in Pattern | Class in Project | Responsibilities |
| :--- | :--- | :--- |
| **Product** | `PaymentProcessor` | Interface defining operations common to all products. |
| **Concrete Products** | `CreditCardPaymentProcessor`, `PayPalPaymentProcessor`, `CashPaymentProcessor` | Specific implementations of the product interface. |
| **Creator** | `PaymentProcessorFactory` / `CheckoutService` | Declares the factory method and core business logic. |
| **Concrete Creators** | `CreditCardPaymentFactory`, `PayPalPaymentFactory`, `OnlineCheckoutService`, etc. | Overrides the factory method to return concrete product instances. |

### Core Principle Illustrated:
> *"Factory Methods are usually called from within Template Methods."* — Gang of Four
> 
> The base class controls **when** the object is used, but the subclasses decide **which** object is created.

---

## 5. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/src/Main.java))
```java
public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-202");
        order.addProduct(new Product("Mechanical Keyboard", new BigDecimal("70.00")));
        order.addProduct(new Product("Gaming Mouse",        new BigDecimal("30.00")));
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));

        // 1. Standalone Factory Method
        PaymentProcessorFactory cardFactory = new CreditCardPaymentFactory();
        cardFactory.processPayment(order.getId(), order.getTotal());

        PaymentProcessorFactory paypalFactory = new PayPalPaymentFactory();
        paypalFactory.processPayment(order.getId(), order.getTotal());

        // 2. Checkout Pipeline Factory Method
        CheckoutService onlineCheckout = new OnlineCheckoutService();
        onlineCheckout.processOrder(order); // Uses CreditCardPaymentProcessor

        CheckoutService paypalCheckout = new PayPalCheckoutService();
        paypalCheckout.processOrder(order); // Uses PayPalPaymentProcessor
    }
}
```

### How to Compile & Run
```bash
cd factory-method-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/payment/*.java src/checkout/*.java src/*.java
java -cp src Main
```

---

## 6. When to Use the Factory Method Pattern

Use the Factory Method Pattern when:
1. **You don't know ahead of time the exact types of objects your code will need:** When adding a new product requires only writing a new subclass without touching existing classes.
2. **You want to provide users of your library/framework a way to extend its internal components:** Subclasses can provide their own versions of components.
3. **You want to eliminate switch / if-else cascades:** Replace conditional creation code with clean polymorphic dispatch.

---

## 7. Other Real-World Use Cases

* **Java Standard Library (JDK):**
  * `java.util.Collection.iterator()`: `Collection` declares the factory method `iterator()`. Subclasses like `ArrayList` and `HashSet` override it to return their own `Iterator` implementations (`ArrayList$Itr`, `HashMap$KeyIterator`).
  * `java.net.URLStreamHandlerFactory`: Used by the Java networking stack to instantiate protocol-specific URL stream handlers (HTTP, HTTPS, FTP, File).
* **Spring Framework:**
  * `FactoryBean<T>`: Spring's interface containing the factory method `getObject()`, allowing custom factory beans to create complex configured objects.
* **Logging Frameworks:**
  * `LoggerFactory.getLogger()`: While called statically, internal logging binders use factory methods to instantiate loggers for Logback, Log4j, or Console.
* **Document Exporting & Cross-Platform UI:**
  * An `Application` class declaring `createDocument()`, where `WordApplication` creates a `WordDocument` and `PdfApplication` creates a `PdfDocument`.
