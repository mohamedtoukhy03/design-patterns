import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

/**
 * Adapts the legacy bank's REST API to the {@link PaymentProcessor} interface
 * that the payment system expects.
 * <p>
 * The payment system only knows how to call {@code pay(orderId, amount)}.
 * The bank exposes {@code POST /api/transactions} and expects a JSON body with an
 * account reference and a currency, and it answers with an HTTP status code.
 * This class translates between the two:
 * <ul>
 *   <li>a {@code pay(...)} call becomes an HTTP request with a JSON body</li>
 *   <li>the extra values the bank needs ({@code accountRef}, currency) are added here</li>
 *   <li>a non-{@code 201} response or a network error becomes a
 *       {@link PaymentException}</li>
 * </ul>
 * The rest of the application never sees HTTP, JSON, or the bank's response format.
 *
 * @see PaymentProcessor
 */
public class LegacyBankAdapter implements PaymentProcessor {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient client;
    private final String baseUrl;
    private final String accountRef;

    public LegacyBankAdapter(String baseUrl, String accountRef, HttpClient client) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("baseUrl must not be null or blank");
        }
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.accountRef = accountRef == null ? "" : accountRef;
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    public LegacyBankAdapter(String baseUrl, String accountRef) {
        this(baseUrl, accountRef, HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build());
    }

    @Override
    public void pay(String orderId, BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }

        String json = """
                {"accountRef":"%s","amount":%s,"currencyCode":"USD"}
                """.formatted(escapeJson(accountRef), amount.toPlainString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/transactions"))
                .timeout(TIMEOUT)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                throw new PaymentException(
                        "Order " + orderId + " rejected by bank (" + response.statusCode() + "): "
                                + response.body());
            }
            System.out.println("Order " + orderId + " paid: " + response.body());

        } catch (IOException e) {
            throw new PaymentException("Could not reach the bank at " + baseUrl + ": " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PaymentException("Payment was interrupted", e);
        }
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

