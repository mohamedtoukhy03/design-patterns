# ☕ Real-World Java Design Patterns

> Practical, production-grade implementations of classic **Gang of Four (GoF)** design patterns in modern Java and Spring Boot.

Instead of textbook toy examples (like shapes or animals), this repository demonstrates design patterns solving **realistic, enterprise-level problems** — such as legacy REST API integration, thread-safe configuration management, and decoupled domain services.

---

## 📚 Pattern Catalog

| Pattern | Category | Description | Real-World Scenario |
| :--- | :--- | :--- | :--- |
| **[Adapter Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/README.md)** | Structural | Converts an incompatible interface into an interface clients expect without modifying existing code. | Adapts an external Legacy Banking REST API (`POST /api/transactions`) to an internal checkout `PaymentProcessor` interface. |
| **[Singleton Pattern](file:///home/mohamedtoukhy/Java/DesginPattern/singleton-pattern/src/AppConfig.java)** | Creational | Guarantees a class has only one instance and provides a global access point to it. | Thread-safe, lazy-initialized application configuration (`AppConfig`) using double-checked locking and environment variables. |

---

## 🛠️ Pattern Spotlights

### 1. Adapter Pattern (`adapter-pattern`)
* **Problem**: The checkout service expects a clean `PaymentProcessor.pay(orderId, amount)`. However, the bank service is an external REST API requiring HTTP POST, JSON payloads, merchant account numbers (`accountRef`), and specific currency codes (`USD`).
* **Solution**: `LegacyBankAdapter` implements `PaymentProcessor` and encapsulates the HTTP requests, parameter mapping, and exception translation.
* **Tech Stack**: Java 21+, Spring Boot (Validation, MockMvc), `java.net.http.HttpClient`.

```
[CheckoutService] ---> [PaymentProcessor] <|--- [LegacyBankAdapter] ---> (HTTP POST) ---> [LegacyBank API]
```

### 2. Singleton Pattern (`singleton-pattern`)
* **Problem**: An application configuration manager must be instantiated exactly once, load environment variables lazily, and remain safe under high multi-threaded contention without blocking read access.
* **Solution**: Double-checked locking with a `volatile` instance variable and private constructor.

---

## 🚀 Getting Started

### Prerequisites
* **Java**: JDK 21 or higher
* **Maven**: 3.9+ (or included Maven wrapper `./mvnw`)

### Running the Examples

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

#### Singleton Pattern
Run the configuration verification:
```bash
javac singleton-pattern/src/*.java
java -cp singleton-pattern/src Main
```

---

## 🗺️ Roadmap
- [ ] **Strategy Pattern** (Dynamic discount & payment routing)
- [ ] **Factory Method / Abstract Factory** (Cross-platform document export)
- [ ] **Decorator Pattern** (Auditing & caching wrapper for repository services)
- [ ] **Observer Pattern** (Event-driven notification system)

