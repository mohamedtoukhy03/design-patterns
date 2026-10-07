import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        String bankUrl = "http://localhost:8080";

        // 1. Successful payment with a valid account reference
        CheckoutService checkout =
                new CheckoutService(new LegacyBankAdapter(bankUrl, "ACC-123"));
        attempt(checkout, "ORD-1", "49.99");                 // paid (201 Created)

        // 2. Rejected payment: bank expects a non-blank accountRef, returns 400 Bad Request
        CheckoutService blankAccount =
                new CheckoutService(new LegacyBankAdapter(bankUrl, ""));
        attempt(blankAccount, "ORD-2", "10.00");             // bank answers 400
    }

    private static void attempt(CheckoutService checkout, String orderId, String amount) {
        try {
            checkout.checkout(orderId, new BigDecimal(amount));
        } catch (PaymentException e) {
            System.out.println("FAILED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}

