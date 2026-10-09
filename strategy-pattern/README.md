# 🎯 Strategy Pattern

> **Category:** Behavioral Design Pattern

---

## 1. What is the Strategy Pattern?

The **Strategy Pattern** defines a family of algorithms, encapsulates each one inside a separate class, and makes them **interchangeable**. It lets the algorithm vary independently from the clients that use it.

### The Problem It Solves
Consider an e-commerce platform that calculates shipping rates based on various shipping tiers. A naive implementation places all calculation rules inside the [`Order`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/model/Order.java) class using a large `if/else` or `switch` block:

```java
// ❌ Problematic Approach:
public BigDecimal calculateShipping(String type) {
    if ("STANDARD".equals(type)) {
        // rule 1...
    } else if ("EXPRESS".equals(type)) {
        // rule 2...
    } else if ("INTERNATIONAL".equals(type)) {
        // rule 3...
    }
}
```

**Why this breaks down in real systems:**
* **Violates Single Responsibility Principle (SRP):** `Order` should represent order state, not become a dumping ground for every shipping company's tariff formulas.
* **Violates Open/Closed Principle (OCP):** Every time business adds a new courier or promotional discount, you must modify and re-test `Order.java`.
* **Testing Nightmare:** You cannot unit test complex international customs or overnight courier calculations in isolation.

---

## 2. The Code Example (Shipping Calculator)

In this project, the calculation logic is extracted into interchangeable strategy classes.

```text
               +-------------------+
               |       Order       |  (Context)
               +-------------------+
               | - strategy        |
               +-------------------+
                         |
                         v delegates to
               +-------------------+
               | <<interface>>     |
               | ShippingStrategy  |  (Strategy Interface)
               +-------------------+
                         ^
         +---------------+---------------+---------------+
         |                               |               |
+------------------+           +------------------+    +-------------------+
| StandardShipping |           |  ExpressShipping |    | NextDayShipping   |
+------------------+           +------------------+    +-------------------+
```

### Components:

1. **Strategy Interface ([`ShippingStrategy.java`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/strategy/ShippingStrategy.java))**:
   Declares the common operation. Notice that passing the `Order` context gives concrete strategies access to whatever fields they need:
   ```java
   public interface ShippingStrategy {
       BigDecimal calculate(Order order);
   }
   ```

2. **Concrete Strategies ([`strategy/`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/strategy/))**:
   Each implements a distinct pricing rule:
   * **`StandardShipping`**: Base flat fee of `$5.00`, but **FREE (`$0.00`)** if items total $\ge \$100.00$.
   * **`ExpressShipping`**: Base fee of `$15.00` + `$2.00`/kg surcharge for parcels over $5\text{ kg}$.
   * **`InternationalShipping`**: Base fee of `$20.00` + `$4.00`/kg air freight rate.
   * **`NextDayShipping`**: Base fee of `$25.00` + `$5.00` transit insurance for orders $\ge \$200.00$.

3. **Context Class ([`Order.java`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/model/Order.java))**:
   Maintains a reference to a `ShippingStrategy` and delegates the calculation to it:
   ```java
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
           return shippingStrategy.calculate(this); // Delegation
       }

       public BigDecimal getTotal() {
           return itemsTotal.add(getShippingCost());
       }
   }
   ```

---

## 3. How the Example Reflects the Design Pattern

| Strategy Role | Class in Project | Explanation |
| :--- | :--- | :--- |
| **Strategy Interface** | `ShippingStrategy` | The contract defining the algorithm execution method `calculate(Order)`. |
| **Concrete Strategies** | `StandardShipping`, `ExpressShipping`, `InternationalShipping`, `NextDayShipping` | Separate encapsulated algorithm classes. Each implements its own business rules. |
| **Context** | `Order` | Maintains state (`itemsTotal`, `weight`) and invokes the active strategy without knowing the formula. |
| **Dynamic Swapping** | `order.setShippingStrategy(...)` | Enables changing shipping algorithms on the fly without reconstructing the `Order`. |

---

## 4. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/Main.java))
```java
public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1", new BigDecimal("80.00"), new BigDecimal("2.5"));

        // 1. Default strategy: Standard Shipping ($5.00)
        System.out.println("Standard:      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Switch to Express ($15.00)
        order.setShippingStrategy(new ExpressShipping());
        System.out.println("Express:       " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 3. Switch to International ($20.00 base + 4.00 * 2.5kg = $30.00)
        order.setShippingStrategy(new InternationalShipping());
        System.out.println("International: " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 4. Switch to NextDay Priority ($25.00)
        order.setShippingStrategy(new NextDayShipping());
        System.out.println("NextDay:       " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
```

### How to Compile & Run
```bash
cd strategy-pattern
javac src/model/*.java src/strategy/*.java src/*.java
java -cp src Main
```

---

## 5. When to Use the Strategy Pattern

Use the Strategy Pattern when:
1. **You have multiple variants of an algorithm:** You need different calculations (discounts, taxes, shipping) that can be swapped based on customer tier, region, or time.
2. **You want to eliminate conditional statements:** Replacing nested `if-else` or `switch` branches that decide which algorithm to execute.
3. **Algorithms use data the client shouldn't care about:** Encapsulates algorithm-specific data and formulas away from the context class.
4. **Behavior needs to change at runtime:** The user or system should be able to switch strategies dynamically while the program is running.

---

## 6. Other Real-World Use Cases

* **Java Collections Sorting:**
  * `java.util.Comparator`: `Collections.sort(list, comparator)` takes any `Comparator<T>` strategy to determine ordering.
* **Spring Security Authentication:**
  * `AuthenticationProvider`: Spring delegates authentication to different strategies (LDAP, OAuth2, JWT, InMemory, Database) without modifying core filter chains.
* **Tax Calculation Engines:**
  * E-commerce tax calculation swapping between US Sales Tax, Canadian GST/HST, and European VAT strategies based on shipping destination.
* **File Compression & Archiving:**
  * A backup utility using interchangeable compression strategies (`ZipStrategy`, `GzipStrategy`, `TarStrategy`, `SevenZipStrategy`).
* **Route Planning & GPS Navigation:**
  * Navigation apps calculating routes using `WalkingStrategy`, `DrivingStrategy`, `BicyclingStrategy`, or `PublicTransitStrategy`.

