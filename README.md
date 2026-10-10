# ☕ Real-World Java Design Patterns

> Practical, production-grade implementations of classic **Gang of Four (GoF)** and enterprise design patterns in modern Java and Spring Boot.

Instead of textbook toy examples (like shapes or animals), this repository demonstrates design patterns solving **realistic, enterprise-level problems** — such as legacy REST API integration, thread-safe configuration management, and dynamic decoupled business logic.

---

## 📚 Pattern Catalog

| Pattern | Category | Description | Real-World Scenario |
| :--- | :--- | :--- | :--- |
| **[Singleton Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/singleton-pattern/README.md)** | Creational | Guarantees a class has only one instance and provides a global access point to it. | Thread-safe, lazy-initialized application configuration (`AppConfig`) using double-checked locking, reflection defense, and environment variables. |
| **[Adapter Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/README.md)** | Structural | Converts an incompatible interface into an interface clients expect without modifying existing code. | Adapts an external Legacy Banking Spring Boot REST API (`POST /api/transactions`) to an internal checkout `PaymentProcessor` interface. |
| **[Strategy Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/README.md)** | Behavioral | Defines a family of interchangeable algorithms and lets them vary independently from clients. | Dynamic shipping cost calculator (`Order` context swapping between `Standard`, `Express`, `International`, and `NextDay` at runtime). |
| **[Simple Factory Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/README.md)** | Creational | Centralizes object creation in a dedicated class, shielding clients from concrete implementations. | Decouples order checkout from concrete strategy instantiation via `ShippingStrategyFactory.createStrategy(...)`. |
| **[Null Object Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/README.md)** | Behavioral | Provides a non-functional object as a surrogate for missing or null instances to eliminate null checks. | Returns a zero-cost safe strategy (`ShippingStrategyNullObject`) when null or unselected shipping is passed, avoiding `NullPointerException`. |
| **[Template Method Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/README.md)** | Behavioral | Defines the skeleton of an algorithm in a base method, deferring specific steps to subclasses without altering structure. | Abstract `CheckoutService.processOrder(Order)` orchestrates validation, payment, and dispatch across `Online` and `InStore` channels. |
| **[Factory Method Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/factory-method-pattern/README.md)** | Creational | Defines an interface for creating an object, but lets subclasses decide which class to instantiate. | `PaymentProcessorFactory` and `CheckoutService` delegate payment processor creation to polymorphic subclasses (`CreditCard`, `PayPal`, `Cash`). |

---

## 🛠️ Deep-Dive Pattern Spotlights

### 1. Singleton Pattern (`singleton-pattern`)

#### Problem
An application configuration manager (`AppConfig`) must be instantiated exactly once across the JVM, load environment variables lazily on first access, and remain completely thread-safe under heavy concurrent access without degrading performance with synchronized method locks.

#### Solution
* **Double-Checked Locking**: The instance is checked twice. The synchronization block is only entered on the first call, eliminating thread contention on subsequent calls.
* **`volatile` Modifier**: Prevents CPU instruction reordering, ensuring that other threads never observe a partially initialized object.
* **Reflection Protection**: The private constructor verifies that `instance == null`, throwing `IllegalStateException` if reflection attempts to construct a second instance.

#### Project Layout
```text
singleton-pattern/src/
├── AppConfig.java        <-- Thread-safe singleton with double-checked locking
└── Main.java             <-- Verifies single instance identity (a == b)
```

#### How to Run
```bash
cd singleton-pattern
javac src/*.java
java -cp src Main
```

---

### 2. Adapter Pattern (`adapter-pattern`)

#### Problem
An e-commerce checkout service expects a clean domain interface: `PaymentProcessor.pay(orderId, amount)`. However, the bank service is an external Legacy REST API requiring HTTP POST requests, JSON serialization, merchant account numbers (`accountRef`), and ISO currency codes (`USD`), returning HTTP 201 Created or 400 Bad Request.

#### Solution (Object Adapter)
[`LegacyBankAdapter`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/LegacyBankAdapter.java) implements [`PaymentProcessor`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/PaymentProcessor.java), encapsulates the HTTP client, handles parameter translation, normalizes endpoints, and translates HTTP status codes and network failures into domain `PaymentException`s.

```text
[CheckoutService] ---> [PaymentProcessor] <|--- [LegacyBankAdapter] ---> (HTTP POST) ---> [LegacyBank REST API]
```

#### Components
* **Target Interface**: [`PaymentProcessor`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/PaymentProcessor.java) (`void pay(String orderId, BigDecimal amount)`)
* **Client**: [`CheckoutService`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/CheckoutService.java)
* **Adaptee**: Spring Boot microservice [`BankController`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/LegacyBank/src/main/java/com/example/legacybank/controller/BankController.java) (`POST /api/transactions`)
* **Adapter**: [`LegacyBankAdapter`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/LegacyBankAdapter.java)

#### Project Layout
```text
adapter-pattern/
├── LegacyBank/                     <-- Spring Boot banking microservice (Adaptee)
│   ├── src/main/java/com/example/legacybank/
│   │   ├── controller/BankController.java
│   │   ├── dto/request/TransactionRequest.java
│   │   └── exception/GlobalExceptionHandler.java
│   └── src/test/java/...           <-- MockMvc integration tests
└── PaymentSystem/                  <-- Client domain & Adapter
    └── src/
        ├── CheckoutService.java    <-- Client
        ├── PaymentProcessor.java   <-- Target interface
        ├── LegacyBankAdapter.java  <-- Adapter
        ├── PaymentException.java   <-- Domain exception
        └── Main.java               <-- Client demo
```

#### How to Run
```bash
# Terminal 1: Start Legacy Bank microservice
cd adapter-pattern/LegacyBank
./mvnw spring-boot:run

# Terminal 2: Run Payment System checkout
cd adapter-pattern
javac PaymentSystem/src/*.java
java -cp PaymentSystem/src Main
```

---

### 3. Strategy Pattern (`strategy-pattern`)

#### Problem
An e-commerce [`Order`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/model/Order.java) needs to calculate shipping costs based on varying commercial rules (flat rate, courier weight limits, international customs, priority overnight). Embedding this logic inside `Order` with `if/else` chains violates both the **Single Responsibility Principle** and the **Open/Closed Principle**.

#### Solution
The [`Order`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/model/Order.java) context references a [`ShippingStrategy`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/strategy/ShippingStrategy.java) interface and delegates calculation via `shippingStrategy.calculate(this)`. Strategies inspect only the attributes they need:

| Strategy | Real-World Rule | Method Inspected |
| :--- | :--- | :--- |
| **`StandardShipping`** | Flat $5.00, or **FREE ($0.00)** if order total $\ge \$100.00$. | `order.getItemsTotal()` |
| **`ExpressShipping`** | Fast courier delivery: Flat $15.00. | Tier rate |
| **`InternationalShipping`** | Global cross-border delivery: Flat $30.00. | Tier rate |
| **`NextDayShipping`** | Priority overnight delivery: Flat $25.00. | Tier rate |

```text
[Order Context] ---> [ShippingStrategy] <|--- [StandardShipping]
                                        <|--- [ExpressShipping]
                                        <|--- [InternationalShipping]
                                        <|--- [NextDayShipping]
```

#### Project Layout
```text
strategy-pattern/src/
├── model/
│   ├── Product.java                 <-- Purchased item (name, price)
│   └── Order.java                   <-- Context class holding products
├── strategy/
│   ├── ShippingStrategy.java        <-- Strategy interface
│   ├── StandardShipping.java        <-- Concrete strategy 1
│   ├── ExpressShipping.java         <-- Concrete strategy 2
│   ├── InternationalShipping.java   <-- Concrete strategy 3
│   └── NextDayShipping.java         <-- Concrete strategy 4
└── Main.java                        <-- Client showing runtime strategy swapping
```

#### How to Run
```bash
cd strategy-pattern
javac src/model/*.java src/strategy/*.java src/*.java
java -cp src Main
```

---

### 4. Simple Factory Pattern (`simple-factory-pattern`)

#### Problem
In a pure Strategy Pattern, the client (`Main`) is forced to directly instantiate concrete classes (`new ExpressShipping()`, `new StandardShipping()`), tightly coupling the client to every strategy implementation.

#### Solution
[`ShippingStrategyFactory`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/factory/ShippingStrategyFactory.java) centralizes object instantiation. The client interacts exclusively with the factory using the type-safe [`ShippingType`](file:///home/mohamedtoukhy/Java/DesginPattern/simple-factory-pattern/src/factory/ShippingType.java) enum, decoupling the client completely from concrete implementations.

```text
[Client] ---> asks ---> [ShippingStrategyFactory] ---> instantiates ---> [Concrete Strategy]
   |                                                                             ^
   +------------------------------ passes to Order ------------------------------+
```

#### Code in Action
```java
// Client never calls 'new ExpressShipping()' directly:
order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.EXPRESS));
order.setShippingStrategy(ShippingStrategyFactory.createStrategy(ShippingType.NEXT_DAY));
```

##### Project Layout
```text
simple-factory-pattern/src/
├── model/
│   ├── Product.java                 <-- Purchased item (name, price)
│   └── Order.java                   <-- Context class
├── strategy/
│   ├── ShippingStrategy.java        <-- Product interface
│   ├── StandardShipping.java        <-- Concrete product 1
│   ├── ExpressShipping.java         <-- Concrete product 2
│   ├── InternationalShipping.java   <-- Concrete product 3
│   └── NextDayShipping.java         <-- Concrete product 4
├── factory/
│   ├── ShippingType.java            <-- Type selector enum
│   └── ShippingStrategyFactory.java <-- Simple Factory
└── Main.java                        <-- Decoupled client
```

#### How to Run
```bash
cd simple-factory-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/*.java
java -cp src Main
```

---

### 5. Null Object Pattern (`null-object-pattern`)

#### Problem
If a client passes `null` or an unsupported shipping option, calling `order.getShippingCost()` or `order.getTotal()` causes a runtime `NullPointerException` unless every caller performs defensive `if (strategy != null)` checks throughout the codebase.

#### Solution
[`ShippingStrategyNullObject`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/strategy/ShippingStrategyNullObject.java) implements [`ShippingStrategy`](file:///home/mohamedtoukhy/Java/DesginPattern/null-object-pattern/src/strategy/ShippingStrategy.java) providing neutral, do-nothing behavior (returns `0.00` shipping fee). When `ShippingStrategyFactory.createStrategy(null)` is called, it returns this null object instead of returning `null`, guaranteeing null safety across the entire calculation chain.

```text
[Client] ---> asks for null strategy ---> [ShippingStrategyFactory] ---> returns [ShippingStrategyNullObject]
                                                                                          |
                                                                             calculate() returns $0.00
                                                                             (No NullPointerException!)
```

#### Project Layout
```text
null-object-pattern/src/
├── model/
│   ├── Product.java                 <-- Purchased item (name, price)
│   └── Order.java                   <-- Context class
├── strategy/
│   ├── ShippingStrategy.java        <-- Strategy interface
│   ├── StandardShipping.java        <-- Concrete strategy 1
│   ├── ExpressShipping.java         <-- Concrete strategy 2
│   ├── InternationalShipping.java   <-- Concrete strategy 3
│   ├── NextDayShipping.java         <-- Concrete strategy 4
│   └── ShippingStrategyNullObject.java <-- Null Object implementation
├── factory/
│   ├── ShippingType.java            <-- Type selector enum
│   └── ShippingStrategyFactory.java <-- Returns Null Object on null input
└── Main.java                        <-- Client demo verifying safe fallback
```

#### How to Run
```bash
cd null-object-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/*.java
java -cp src Main
```

---

### 6. Template Method Pattern (`template-pattern`)

#### Problem
Processing customer orders involves a fixed sequence of steps (validate order, calculate total, process payment, dispatch goods, and print receipt). However, different sales channels implement specific steps differently (e.g. online orders charge credit cards and ship via courier, while in-store orders take cash/POS and hand goods across the counter). Copying the algorithm across classes creates code duplication.

#### Solution
[`CheckoutService`](file:///home/mohamedtoukhy/Java/DesginPattern/template-pattern/src/checkout/CheckoutService.java) defines a `final` template method `processOrder(Order)`. Common invariant steps are defined once in the base class, while specific steps (`processPayment`, `dispatchOrder`) are deferred as abstract methods to concrete subclasses (`OnlineCheckoutService`, `InStoreCheckoutService`).

```text
               +--------------------------------------+
               |      <<abstract>> CheckoutService    |
               +--------------------------------------+
               | + processOrder(Order): void  (final) |-----> Invariant Workflow
               | # processPayment(Order): void (abs)  |
               | # dispatchOrder(Order): void  (abs)  |
               +--------------------------------------+
                                   ^
                  +----------------+----------------+
                  |                                 |
+------------------------------------+   +------------------------------------+
|       OnlineCheckoutService        |   |       InStoreCheckoutService       |
+------------------------------------+   +------------------------------------+
```

#### Project Layout
```text
template-pattern/src/
├── model/
│   ├── Product.java                 <-- Item model (name, price)
│   └── Order.java                   <-- Context class holding items
├── payment/
│   └── PaymentProcessor.java        <-- Payment interface (matching Adapter Pattern)
├── checkout/
│   ├── CheckoutService.java         <-- Abstract template defining workflow
│   ├── OnlineCheckoutService.java   <-- Online channel implementation
│   └── InStoreCheckoutService.java  <-- In-store POS implementation
├── factory/                         <-- Strategy factory
├── strategy/                        <-- Reused shipping strategies
└── Main.java                        <-- Multi-channel checkout demo
```

#### How to Run
```bash
cd template-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/payment/*.java src/checkout/*.java src/*.java
java -cp src Main
```

---

### 7. Factory Method Pattern (`factory-method-pattern`)

#### Problem
In a Simple Factory, a single class contains conditional `switch` or `if/else` checks to instantiate objects. Whenever a new payment provider (e.g. Apple Pay, Crypto) is added, that factory class must be modified, violating the **Open/Closed Principle (OCP)**.

#### Solution
The **Factory Method Pattern** replaces centralized `switch` statements with inheritance and polymorphism. The abstract creator defines a factory method (`createProcessor()`), deferring instantiation to specialized subclasses (`CreditCardPaymentFactory`, `PayPalPaymentFactory`, `CashPaymentFactory`, or within `CheckoutService` subclasses).

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
```

#### Project Layout
```text
factory-method-pattern/src/
├── payment/
│   ├── PaymentProcessor.java        <-- Product interface
│   ├── CreditCardPaymentProcessor.java
│   ├── PayPalPaymentProcessor.java
│   ├── CashPaymentProcessor.java
│   ├── PaymentProcessorFactory.java <-- Base creator declaring factory method
│   ├── CreditCardPaymentFactory.java
│   ├── PayPalPaymentFactory.java
│   └── CashPaymentFactory.java
├── checkout/
│   ├── CheckoutService.java         <-- Pipeline defining createPaymentProcessor()
│   ├── OnlineCheckoutService.java   <-- Subclass creates CreditCard processor
│   ├── PayPalCheckoutService.java   <-- Subclass creates PayPal processor
│   └── InStoreCheckoutService.java  <-- Subclass creates Cash processor
├── factory/                         <-- Strategy factory
├── model/                           <-- Order and Product models
├── strategy/                        <-- Reused shipping strategies
└── Main.java                        <-- Client demo showing polymorphic creation
```

#### How to Run
```bash
cd factory-method-pattern
javac src/model/*.java src/strategy/*.java src/factory/*.java src/payment/*.java src/checkout/*.java src/*.java
java -cp src Main
```

---

## 🗺️ Roadmap
- [x] **Singleton Pattern** (Thread-safe configuration with double-checked locking)
- [x] **Adapter Pattern** (Legacy REST banking integration)
- [x] **Strategy Pattern** (Dynamic shipping cost calculator)
- [x] **Simple Factory Pattern** (Centralized strategy instantiation)
- [x] **Null Object Pattern** (Zero-cost safe fallback strategy)
- [x] **Template Method Pattern** (Multi-channel checkout processing pipeline)
- [x] **Factory Method Pattern** (Polymorphic payment processor creation)
- [ ] **Abstract Factory Pattern** (Cross-platform family suites)
- [ ] **Decorator Pattern** (Auditing & caching wrapper for repository services)
- [ ] **Observer Pattern** (Event-driven notification system)
