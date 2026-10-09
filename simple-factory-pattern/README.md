# 🏭 Simple Factory Pattern

> **Category:** Creational Design Pattern

---

## 1. What is the Simple Factory Pattern?

The **Simple Factory Pattern** centralizes the creation of objects in a dedicated factory class or static method, rather than having client code instantiate concrete classes directly with the `new` operator.

### The Problem It Solves
In a pure Strategy Pattern implementation, client code typically writes:
```java
// ❌ Client is tightly coupled to every concrete implementation:
order.setShippingStrategy(new StandardShipping());
order.setShippingStrategy(new ExpressShipping());
order.setShippingStrategy(new InternationalShipping());
```

**Why this becomes problematic:**
1. **Tight Coupling:** The client must import and know about every single concrete class. If class constructors change, every client breaking changes across the codebase.
2. **Duplicated Creation Logic:** If five different controllers or services need to instantiate a shipping strategy based on user input, the `switch` or `if/else` logic is duplicated in five places.
3. **Leaky Implementation Details:** The client should only care about *what* the strategy does (`calculate`), not *how* it is constructed.

---

## 2. The Code Example (Decoupled Strategy Creation)

The Simple Factory encapsulates strategy creation behind [`ShippingStrategyFactory`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/factory/ShippingStrategyFactory.java) using a type-safe [`ShippingType`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/factory/ShippingType.java) enum.

```text
[Client: Main] ---> asks ---> [ShippingStrategyFactory] ---> instantiates ---> [Concrete Strategy]
      |                                                                                  ^
      +----------------------------- passes strategy to Order ---------------------------+
```

### Components:

1. **Selector Enum ([`ShippingType.java`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/factory/ShippingType.java))**:
   Represents all valid shipping options:
   ```java
   public enum ShippingType {
       STANDARD,
       EXPRESS,
       INTERNATIONAL,
       NEXT_DAY
   }
   ```

2. **Simple Factory ([`ShippingStrategyFactory.java`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/factory/ShippingStrategyFactory.java))**:
   Contains the sole creation logic:
   ```java
   public class ShippingStrategyFactory {

       public static ShippingStrategy createStrategy(ShippingType type) {
           if (type == null) {
               return new StandardShipping();
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

3. **Client ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/Main.java))**:
   Notice that `Main` **never imports or calls `new` on any concrete strategy**. It only interacts with the factory and the interface:
   ```java
   order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
   ```

---

## 3. How the Example Reflects the Design Pattern

| Role | Class in Project | Responsibilities |
| :--- | :--- | :--- |
| **Product Interface** | `ShippingStrategy` | Common interface shared by all created objects. |
| **Concrete Products** | `StandardShipping`, `ExpressShipping`, etc. | The actual implementations instantiated by the factory. |
| **Simple Factory** | `ShippingStrategyFactory` | Static factory method creating and returning the concrete product. |
| **Client** | `Main` / `Order` | Requests products through the factory and interacts strictly with the `ShippingStrategy` interface. |

### Key Improvements Over Direct Instantiation:
* **Single Point of Change:** If `ExpressShipping` requires constructor arguments tomorrow (e.g. courier API key), only `ShippingStrategyFactory` needs to be modified. Zero changes are needed in `Order` or `Main`.
* **Complete Encapsulation:** Clients do not even need package-level visibility into concrete implementations.

---

## 4. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/Main.java))
```java
public class Main {
    public static void main(String[] args) {
        Order order = new Order("ORD-1", new BigDecimal("80.00"), new BigDecimal("2.5"));

        // 1. Default (Standard Shipping: $5.00)
        System.out.println("Default:      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 2. Factory creates Express Shipping ($15.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
        System.out.println("Express:      " + order.getShippingCost() + " | Total: " + order.getTotal());

        // 3. Factory creates International Shipping ($30.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.INTERNATIONAL));
        System.out.println("International:" + order.getShippingCost() + " | Total: " + order.getTotal());

        // 4. Factory creates NextDay Shipping ($25.00)
        order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.NEXT_DAY));
        System.out.println("NextDay:      " + order.getShippingCost() + " | Total: " + order.getTotal());
    }
}
```

### How to Compile & Run
```bash
cd simple-factory-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/*.java
java -cp src Main
```

---

## 5. When to Use the Simple Factory Pattern

Use the Simple Factory Pattern when:
1. **You want to decouple clients from concrete classes:** Clients should depend on an interface and not worry about specific implementation details.
2. **Object creation involves conditions or configurations:** If selecting which class to instantiate depends on parameters, configuration flags, or environment properties.
3. **You want to centralize constructor changes:** If a constructor signature changes, you only want to update one factory method rather than search-and-replace across your entire codebase.

---

## 6. Other Real-World Use Cases

* **Java Standard Library (JDK):**
  * `java.text.NumberFormat.getInstance()`: Returns a locale-specific number formatter.
  * `java.util.Calendar.getInstance()`: Returns a `GregorianCalendar` or locale-specific calendar.
  * `javax.swing.BorderFactory.createEmptyBorder()`: Returns customized Swing borders.
* **Database Drivers:**
  * `java.sql.DriverManager.getConnection(url)`: Inspects JDBC connection strings and instantiates the correct vendor driver connection.
* **Notification Dispatchers:**
  * A `NotificationFactory.create(NotificationType.SMS)` returning `SmsNotifier`, `EmailNotifier`, or `PushNotifier`.
* **Document Exporters / Parsers:**
  * `DocumentParserFactory.create(FileType.CSV)` returning a `CsvParser`, `JsonParser`, or `XmlParser`.
* **Payment Gateways:**
  * `PaymentGatewayFactory.create("STRIPE")` returning the configured Stripe payment gateway.

