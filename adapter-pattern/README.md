# 🔌 Adapter Pattern

> **Category:** Structural Design Pattern

---

## 1. What is the Adapter Pattern?

The **Adapter Pattern** (also known as a **Wrapper**) allows classes with incompatible interfaces to collaborate. It acts as a bridge between two objects, converting the interface of a class into another interface that clients expect.

### The Problem It Solves
Modern software systems constantly need to integrate with external systems, legacy codebases, or third-party vendor libraries:
* The client code depends on a clean domain contract (e.g. `pay(orderId, amount)`).
* The external or legacy service expects an entirely different protocol, data structure, or method signature (e.g. HTTP REST POST with JSON bodies, merchant tokens, currency codes, and status codes).
* Rewriting the client code to fit every external vendor tightly couples your application to third-party APIs. Rewriting the external service is impossible because you do not own it.
* The Adapter Pattern solves this by placing an intermediary class between the client and the adaptee.

---

## 2. The Code Example (Legacy Bank REST Integration)

In this project, our e-commerce checkout service needs to charge customers via an external **Legacy Banking Spring Boot REST API**.

```text
[CheckoutService] ---> [PaymentProcessor] <|--- [LegacyBankAdapter] ---> (HTTP POST JSON) ---> [LegacyBank REST API]
     (Client)              (Target)                  (Adapter)                                      (Adaptee)
```

### Components:

1. **Target Interface ([`PaymentProcessor.java`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/PaymentProcessor.java))**:
   The domain contract expected by our checkout system:
   ```java
   public interface PaymentProcessor {
       void pay(String orderId, BigDecimal amount);
   }
   ```

2. **Client ([`CheckoutService.java`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/CheckoutService.java))**:
   Depends exclusively on the `PaymentProcessor` interface:
   ```java
   public class CheckoutService {
       private final PaymentProcessor processor;

       public CheckoutService(PaymentProcessor processor) {
           this.processor = Objects.requireNonNull(processor);
       }

       public void checkout(String orderId, BigDecimal total) {
           // Business logic...
           processor.pay(orderId, total);
       }
   }
   ```

3. **Adaptee ([`BankController.java`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/LegacyBank/src/main/java/com/example/legacybank/controller/BankController.java))**:
   A Spring Boot microservice exposing `POST /api/transactions` with a JSON payload:
   ```json
   {
     "accountRef": "ACC-9921",
     "amount": 100.50,
     "currencyCode": "USD"
   }
   ```

4. **Adapter ([`LegacyBankAdapter.java`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/LegacyBankAdapter.java))**:
   Implements `PaymentProcessor`, wraps `java.net.http.HttpClient`, serializes the JSON request, executes the HTTP call, and maps HTTP 201 / 400 status codes into domain exceptions:
   ```java
   public class LegacyBankAdapter implements PaymentProcessor {
       private final HttpClient client;
       private final String baseUrl;
       private final String accountRef;

       public LegacyBankAdapter(String baseUrl, String accountRef) { ... }

       @Override
       public void pay(String orderId, BigDecimal amount) {
           String json = """
               {"accountRef":"%s","amount":%s,"currencyCode":"USD"}
               """.formatted(escapeJson(accountRef), amount.toPlainString());

           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create(baseUrl + "/api/transactions"))
                   .header("Content-Type", "application/json")
                   .POST(HttpRequest.BodyPublishers.ofString(json))
                   .build();

           HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

           if (response.statusCode() != 201) {
               throw new PaymentException("Bank rejected payment: " + response.body());
           }
       }
   }
   ```

---

## 3. How the Example Reflects the Design Pattern

| Adapter Role | Project Class | Responsibilities |
| :--- | :--- | :--- |
| **Target** | `PaymentProcessor` | The interface client code understands (`pay(orderId, amount)`). |
| **Client** | `CheckoutService` | Collaborates only with `PaymentProcessor`. Completely oblivious to HTTP, JSON, or bank endpoints. |
| **Adaptee** | `LegacyBank` (REST API) | The existing, incompatible system with its own protocol and data model. |
| **Adapter** | `LegacyBankAdapter` | Converts `pay(...)` calls into HTTP POST requests, adds missing parameters (`accountRef`, `currencyCode`), and translates HTTP errors into `PaymentException`. |

### Key Benefits Observed:
* **Single Responsibility Principle (SRP):** Network transport, JSON marshaling, and error mapping are kept inside `LegacyBankAdapter`.
* **Open/Closed Principle (OCP):** If we add a new payment provider (e.g. PayPal, Stripe), we simply write a `StripeAdapter` implementing `PaymentProcessor`. `CheckoutService` requires **zero code changes**.

---

## 4. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/Main.java))
```java
// 1. Create the adapter with bank connection parameters
PaymentProcessor bankAdapter = new LegacyBankAdapter("http://localhost:8080", "ACC-44910");

// 2. Inject adapter into checkout service
CheckoutService checkoutService = new CheckoutService(bankAdapter);

// 3. Process checkout cleanly
checkoutService.checkout("ORD-1001", new BigDecimal("150.00"));
```

### How to Run

```bash
# Terminal 1: Start Legacy Bank microservice
cd adapter-pattern/LegacyBank
./mvnw spring-boot:run

# Terminal 2: Run Payment System client
cd adapter-pattern
javac PaymentSystem/src/*.java
java -cp PaymentSystem/src Main
```

---

## 5. When to Use the Adapter Pattern

Use the Adapter Pattern when:
1. **Integrating third-party or legacy libraries:** You want to use an existing class, but its interface does not match the rest of your application code.
2. **Standardizing multiple vendor integrations:** You need to integrate multiple external providers (e.g., AWS S3, Google Cloud Storage, Azure Blob Storage) behind a single uniform storage interface.
3. **Preventing vendor lock-in:** You want to isolate third-party API changes so changes in the vendor's SDK only impact the adapter class, not your entire domain layer.

---

## 6. Other Real-World Use Cases

* **Java Standard Library (I/O):**
  * `java.io.InputStreamReader`: Adapts a byte-oriented `InputStream` (Adaptee) to a character-oriented `Reader` (Target).
  * `java.util.Arrays.asList()`: Adapts an array into a `List` interface.
* **Spring Framework:**
  * `HandlerAdapter`: Adapts different types of web handlers (controller methods, HttpRequestHandler, Servlet) so Spring's `DispatcherServlet` can invoke them uniformly.
* **Logging Bridges:**
  * `log4j-over-slf4j` / `jcl-over-slf4j`: Adapts calls from Apache Commons Logging or Log4j to SLF4J, redirecting legacy logging calls to modern log backends.
* **JDBC Drivers:**
  * Every JDBC driver adapts standard SQL calls from `java.sql.Connection` / `PreparedStatement` to the database vendor's proprietary network protocol (e.g. Oracle Net8, MySQL wire protocol).
* **Payment & Messaging Gateways:**
  * Adapting PayPal SDK, Stripe API, and Square API into a common enterprise `PaymentGateway` interface.

