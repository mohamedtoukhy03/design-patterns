# Adapter Pattern Example

This module demonstrates the **Adapter Design Pattern** (specifically the Object Adapter variant) in a modern microservice context.

---

## 🏛️ Pattern Components

| Component | Class / Interface | Role |
| :--- | :--- | :--- |
| **Target Interface** | [`PaymentProcessor`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/PaymentProcessor.java) | The interface that the domain/client application expects (`void pay(String orderId, BigDecimal amount)`). |
| **Client** | [`CheckoutService`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/CheckoutService.java) | Business logic that triggers payment without knowing anything about HTTP, JSON, or bank-specific protocols. |
| **Adaptee** | [`BankController`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/LegacyBank/src/main/java/com/example/legacybank/controller/BankController.java) | The existing, incompatible system (Spring Boot REST service exposing `POST /api/transactions` requiring `accountRef`, `amount`, `currencyCode`). |
| **Adapter** | [`LegacyBankAdapter`](file:///home/mohamedtoukhy/Java/DesginPattern/adapter-pattern/PaymentSystem/src/LegacyBankAdapter.java) | Implements `PaymentProcessor`, wraps HTTP client communication, injects bank-required fields, and translates HTTP responses / network errors into domain exceptions. |

---

## 🚀 How to Run

### Step 1: Start the Legacy Bank service
In one terminal window, start the Spring Boot bank application:
```bash
cd adapter-pattern/LegacyBank
./mvnw spring-boot:run
```
The bank service will start on port `8080`.

### Step 2: Run the Payment System
In another terminal window:
```bash
cd adapter-pattern
javac PaymentSystem/src/*.java
java -cp PaymentSystem/src Main
```

### Expected Output
```text
Order ORD-1 paid: {"transactionId":"TX-..."}
FAILED: Order ORD-2 rejected by bank (400): {"timestamp":"...","status":400,"error":"Bad Request","message":"Validation failed","path":"/api/transactions","fieldErrors":{"accountRef":"accountRef is required"}}
```

