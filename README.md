# ☕ Real-World Java Design Patterns

> Practical, production-grade implementations of classic **Gang of Four (GoF)** design patterns in modern Java and Spring Boot.

Instead of textbook toy examples (like shapes or animals), this repository demonstrates design patterns solving **realistic, enterprise-level problems** — such as legacy REST API integration, thread-safe configuration management, and dynamic decoupled business logic.

---

## 📚 Pattern Catalog

| Pattern | Category | Description | Real-World Scenario |
| :--- | :--- | :--- | :--- |
| **[Singleton Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/singleton-pattern/src/AppConfig.java)** | Creational | Guarantees a class has only one instance and provides a global access point to it. | Thread-safe, lazy-initialized application configuration (`AppConfig`) using double-checked locking and environment variables. |
| **[Adapter Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/README.md)** | Structural | Converts an incompatible interface into an interface clients expect without modifying existing code. | Adapts an external Legacy Banking REST API (`POST /api/transactions`) to an internal checkout `PaymentProcessor` interface. |
| **[Strategy Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/ShippingStrategy.java)** | Behavioral | Defines a family of interchangeable algorithms and lets them vary independently from clients. | Dynamic shipping cost calculator (`Order` context swapping between `Standard`, `Express`, `International`, and `NextDay` at runtime). |

---

## 🛠️ Pattern Spotlights

### 1. Strategy Pattern (`strategy-pattern`)
* **Problem**: An e-commerce `Order` must calculate shipping costs based on different pricing models (flat rate, courier weight limits, cross-border customs, priority overnight). Embedding this logic inside `Order` with `if/else` would violate the Single Responsibility and Open/Closed principles.
* **Solution**: An `Order` context holds a reference to a `ShippingStrategy` interface and delegates calculation via `shippingStrategy.calculate(this)`. New shipping rules are added as new classes without modifying `Order`.
* **Classes**:
  * [`ShippingStrategy`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/ShippingStrategy.java) (Strategy Interface)
  * [`StandardShipping`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/StandardShipping.java) (Flat fee + Free shipping threshold)
  * [`ExpressShipping`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/ExpressShipping.java) (Base fee + Overweight surcharge)
  * [`InternationalShipping`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/InternationalShipping.java) (Customs base fee + Per-kg air freight)
  * [`NextDayShipping`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/NextDayShipping.java) (Priority overnight + Transit insurance)
  * [`Order`](file:///home/mohamedtoukhy/Java/DesginPattern/strategy-pattern/src/Order.java) (Context)

```
[Order Context] ---> [ShippingStrategy] <|--- [StandardShipping]
                                        <|--- [ExpressShipping]
                                        <|--- [InternationalShipping]
                                        <|--- [NextDayShipping]
```

### 2. Adapter Pattern (`adapter-pattern`)
* **Problem**: The checkout service expects a clean `PaymentProcessor.pay(orderId, amount)`. However, the bank service is an external REST API requiring HTTP POST, JSON payloads, merchant account numbers (`accountRef`), and specific currency codes (`USD`).
* **Solution**: `LegacyBankAdapter` implements `PaymentProcessor` and encapsulates HTTP communication, JSON mapping, and exception translation.

```
[CheckoutService] ---> [PaymentProcessor] <|--- [LegacyBankAdapter] ---> (HTTP POST) ---> [LegacyBank API]
```

### 3. Singleton Pattern (`singleton-pattern`)
* **Problem**: Application configuration must be instantiated exactly once, load environment variables lazily, and remain thread-safe without blocking read access.
* **Solution**: Double-checked locking with a `volatile` instance variable and private constructor.

---

## 🚀 Getting Started

### Prerequisites
* **Java**: JDK 21 or higher
* **Maven**: 3.9+ (or included Maven wrapper `./mvnw`)

### Running the Examples

#### Strategy Pattern
```bash
cd strategy-pattern
javac src/*.java
java -cp src Main
```

#### Singleton Pattern
```bash
javac singleton-pattern/src/*.java
java -cp singleton-pattern/src Main
```

#### Adapter Pattern
1. Start the Legacy Bank service:
   ```bash
   cd adapter-pattern/LegacyBank
   ./mvnw spring-boot:run
   ```
2. In another terminal, run the checkout client:
   ```bash
   cd adapter-pattern
   javac PaymentSystem/src/*.java
   java -cp PaymentSystem/src Main
   ```

---

## 🗺️ Roadmap
- [x] **Singleton Pattern** (Thread-safe configuration)
- [x] **Adapter Pattern** (Legacy REST banking integration)
- [x] **Strategy Pattern** (Dynamic shipping cost calculator)
- [ ] **Factory Method / Abstract Factory** (Cross-platform document export)
- [ ] **Decorator Pattern** (Auditing & caching wrapper for repository services)
- [ ] **Observer Pattern** (Event-driven notification system)

