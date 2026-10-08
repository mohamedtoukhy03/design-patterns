/**
 * Application configuration singleton.
 * <p>
 * Guarantees only one instance per JVM using lazy initialization
 * and thread-safe Double-Checked Locking with a {@code volatile} reference.
 */
public final class AppConfig {

    // volatile: prevents instruction reordering; ensures all threads see fully-initialized object
    private static volatile AppConfig instance;

    private final String projectName;
    private final String dbUrl;
    private final String dbUserName;
    private final String dbPassword;

    // private: prevents external instantiation via new AppConfig()
    private AppConfig() {
        // Guard against reflection instantiation
        if (instance != null) {
            throw new IllegalStateException("AppConfig instance already exists. Use getInstance().");
        }
        this.projectName = readEnv("APP_NAME", "singleton-pattern");
        this.dbUrl = readEnv("DB_URL", "jdbc:h2:mem:testdb");
        this.dbUserName = readEnv("DB_USER", "user");
        this.dbPassword = readEnv("DB_PASSWORD", "");
    }

    public static AppConfig getInstance() {
        if (instance == null) {                           // First check (no locking overhead)
            synchronized (AppConfig.class) {              // Synchronize block
                if (instance == null) {                   // Second check (thread-safe creation)
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    private static String readEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public String getProjectName() { return projectName; }
    public String getDbUrl()       { return dbUrl; }
    public String getDbUserName()  { return dbUserName; }
    public String getDbPassword()  { return dbPassword; }
}

