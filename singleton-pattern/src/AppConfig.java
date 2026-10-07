/**
 * Application configuration.
 * Only one instance exists per JVM. It is created lazily, the first time
 * {@link #getInstance()} is called, and every later call returns the same object.
 */
public final class AppConfig {

    // volatile: other threads must never see a half-constructed object
    private static volatile AppConfig instance;

    private final String projectName;
    private final String dbUrl;
    private final String dbUserName;
    private final String dbPassword;

    // private: no other class can call new AppConfig(); only this class creates the instance
    private AppConfig() {
        this.projectName = readEnv("APP_NAME", "singleton-pattern");
        this.dbUrl = readEnv("DB_URL", "jdbc:h2:mem:testdb");
        this.dbUserName = readEnv("DB_USER", "user");
        this.dbPassword = readEnv("DB_PASSWORD", "");
    }

    public static AppConfig getInstance() {
        if (instance == null) {                           // no lock, fast path
            synchronized (AppConfig.class) {              // one thread at a time
                if (instance == null) {                   // another thread may have created it
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