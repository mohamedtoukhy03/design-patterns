# 🛡️ Null Object Pattern

> **Category:** Behavioral Design Pattern

---

## 1. What is the Null Object Pattern?

The **Null Object Pattern** provides an object that acts as a surrogate for the lack of an object of a given type. Instead of passing or returning `null` references, you provide a non-functional object that implements the same interface with **neutral, "do-nothing", or safe default behavior**.

### The Problem It Solves
In traditional object-oriented code, when an object reference might be `null`, every client that calls it must remember to perform defensive null checks:

```java
// ❌ Cluttered with defensive checks across the codebase:
if (order.getShippingStrategy() != null) {
    shippingCost = order.getShippingStrategy().calculate(order);
} else {
    shippingCost = BigDecimal.ZERO; // Fallback repeated in many places
}
```

**Why relying on `null` causes issues:**
1. **Pervasive `NullPointerException` (NPE):** Forgetting a single `if (x != null)` check in a nested method call crashes the application at runtime.
2. **Polluted Business Logic:** Methods become bloated with boilerplate null-checks, obscuring the actual domain rules.
3. **Inconsistent Fallbacks:** Different parts of the system might handle a `null` reference differently (one returns 0, another throws an exception, another logs an error).

---

## 2. The Code Example (Safe Shipping Fallback)

In this project, [`ShippingStrategyNullObject`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/strategy/ShippingStrategyNullObject.java) implements [`ShippingStrategy`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/strategy/ShippingStrategy.java) and safely returns `BigDecimal.ZERO`.

```text
[Client] ---> asks for null strategy ---> [ShippingStrategyFactory] ---> returns [ShippingStrategyNullObject]
                                                                                          |
                                                                             calculate() returns 0.00
                                                                             (No NullPointerException!)
```

### Components:

1. **Strategy Interface ([`ShippingStrategy.java`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/strategy/ShippingStrategy.java))**:
   Common interface implemented by both real strategies and the Null Object:
   ```java
   public interface ShippingStrategy {
       BigDecimal calculate(Order order);
   }
   ```

2. **Null Object Class ([`ShippingStrategyNullObject.java`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/strategy/ShippingStrategyNullObject.java))**:
   Implements the contract with neutral, zero-cost behavior:
   ```java
   public class ShippingStrategyNullObject implements ShippingStrategy {
       @Override
       public BigDecimal calculate(Order order) {
           return BigDecimal.ZERO; // Safe neutral value: $0.00
       }
   }
   ```

3. **Factory Fallback ([`ShippingStrategyFactory.java`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/factory/ShippingStrategyFactory.java))**:
   When passed `null` (or unknown), the factory returns the Null Object instead of `null`:
   ```java
   public class ShippingStrategyFactory {
       public static ShippingStrategy createStrategy(ShippingType type) {
           if (type == null) {
               return new ShippingStrategyNullObject(); // Return safe Null Object!
           }

           return switch (type) {
               case STANDARD -> new StandardShipping();
               case EXPRESS -> new ExpressShipping();
               case INTERNATIONAL -> new InternationalShipping();
               case NEXT_DAY -> new NextDayShipping();
           };
       }
   }
   ```

4. **Order Context ([`Order.java`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/model/Order.java))**:
   Can safely invoke `shippingStrategy.calculate(this)` without checking for `null`.

---

## 3. How the Example Reflects the Design Pattern

| Role | Class in Project | Responsibilities |
| :--- | :--- | :--- |
| **Interface** | `ShippingStrategy` | The contract defining the expected operation. |
| **Real Objects** | `StandardShipping`, `ExpressShipping`, etc. | Perform actual pricing calculations. |
| **Null Object** | `ShippingStrategyNullObject` | Provides the do-nothing / zero-fee implementation of `ShippingStrategy`. |
| **Factory / Creator** | `ShippingStrategyFactory` | Shields clients by returning `ShippingStrategyNullObject` whenever input is absent or null. |
| **Client / Context** | `Order` / `Main` | Treats all strategy instances uniformly via polymorphism without defensive `!= null` checks. |

### Contrast:

* **Without Null Object**:
  ```java
  order.setShippingStrategy(null);
  order.getShippingCost(); // 💥 CRASH: java.lang.NullPointerException
  ```

* **With Null Object**:
  ```java
  order.setShippingStrategy(ShippingStrategyFactory.createStrategy(null));
  order.getShippingCost(); // ✅ Returns $0.00 smoothly with no crash
  ```

---

## 4. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/Main.java))
```java
public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1", new BigDecimal("80.00"), new BigDecimal("2.5"));

        // 1. Normal Strategy via Factory: Express ($15.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
        System.out.println("Express:       " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Passing null safely returns the Null Object ($0.00, no NullPointerException)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(null));
        System.out.println("Null Object:   " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
```

### How to Compile & Run
```bash
cd null-object-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/*.java
java -cp src Main
```

---

## 5. When to Use the Null Object Pattern

Use the Null Object Pattern when:
1. **An object requires an optional collaborator:** A class collaborates with an optional dependency (e.g. logger, notification listener, strategy), but you don't want `if (dependency != null)` checks all over the class.
2. **You want to eliminate `NullPointerException`s:** You want your application to behave gracefully even when data or strategies are missing or unselected.
3. **Clients should treat absent and present collaborators identically:** The caller should invoke interface methods without knowing or caring whether the object is a real implementation or a no-op placeholder.

---

## 6. Other Real-World Use Cases

* **Java Standard Library (JDK):**
  * `java.io.InputStream.nullInputStream()` / `OutputStream.nullOutputStream()`: Streams that discard output or return EOF immediately, avoiding null stream checks.
  * `java.util.Collections.emptyList()` / `emptyMap()`: Returning empty collections rather than `null` prevents callers from crashing when iterating.
* **Logging Frameworks:**
  * `NOPLogger` (No-Operation Logger) in SLF4J: When logging is disabled or during tests, SLF4J provides a `NOPLogger` so code calls `log.info(...)` safely without checking if the logger exists.
* **Spring Security & Authentication:**
  * `AnonymousAuthenticationToken`: Rather than returning `null` for unauthenticated requests, Spring Security returns an anonymous token representing an unauthenticated guest.
* **Event Listeners / Callbacks:**
  * UI frameworks and event dispatchers initialize listener callbacks with empty `NullListener` instances so firing events (`listener.onEvent(...)`) never throws an NPE even if no subscriber has registered.
* **Caching Subsystems:**
  * A `NoOpCacheManager` in Spring Cache when caching is turned off for local development or testing.

