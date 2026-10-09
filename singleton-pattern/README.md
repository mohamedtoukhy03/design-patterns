# 🏛️ Singleton Pattern

> **Category:** Creational Design Pattern

---

## 1. What is the Singleton Pattern?

The **Singleton Pattern** ensures that a class has **only one instance throughout the entire application lifecycle** (per JVM) and provides a **single global point of access** to that instance.

### The Problem It Solves
Certain system resources and coordinators must exist in exactly one state across an application. If multiple components create their own separate configuration managers, cache handlers, or thread pools:
* System resources are wasted creating redundant objects.
* Inconsistent states emerge (e.g. Component A reads old configuration while Component B has reloaded it).
* Multi-threaded access can cause race conditions or synchronization overhead if instantiation is not thread-safe.

---

## 2. The Code Example (`AppConfig`)

In this project, [`AppConfig`](file:///home/mohamedtoukhy/Java/DesginPattern/singleton-pattern/src/AppConfig.java) manages application settings (database credentials, URLs, and application metadata).

```java
public final class AppConfig {

    // 1. volatile prevents instruction reordering and ensures thread visibility
    private static volatile AppConfig instance;

    private final String projectName;
    private final String dbUrl;
    private final String dbUserName;
    private final String dbPassword;

    // 2. private constructor stops external instantiation
    private AppConfig() {
        // Guard against Java reflection attacks
        if (instance != null) {
            throw new IllegalStateException("AppConfig instance already exists. Use getInstance().");
        }
        this.projectName = readEnv("APP_NAME", "singleton-pattern");
        this.dbUrl = readEnv("DB_URL", "jdbc:h2:mem:testdb");
        this.dbUserName = readEnv("DB_USER", "user");
        this.dbPassword = readEnv("DB_PASSWORD", "");
    }

    // 3. Double-Checked Locking (DCL) for high performance thread safety
    public static AppConfig getInstance() {
        if (instance == null) {                           // First check: avoids locking once initialized
            synchronized (AppConfig.class) {              // Synchronize block: only during initial creation
                if (instance == null) {                   // Second check: verifies no other thread initialized it
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
```

---

## 3. How the Example Reflects the Design Pattern

| Singleton Element | Implementation in `AppConfig` | Why It Matters |
| :--- | :--- | :--- |
| **Private Constructor** | `private AppConfig()` | Forbids outside classes from calling `new AppConfig()`. |
| **Global Access Method** | `public static AppConfig getInstance()` | The sole door through which the entire application accesses the configuration. |
| **Lazy Initialization** | Instantiated inside `getInstance()` | Does not allocate memory or read environment properties until first requested. |
| **Thread Safety (DCL)** | `if (instance == null) { synchronized... }` | Only locks during the very first call. Subsequent calls read `instance` directly with zero lock contention. |
| **`volatile` Keyword** | `private static volatile AppConfig instance;` | Prevents the CPU / JVM from reordering memory writes, guaranteeing other threads never see a partially initialized object. |
| **Reflection Protection** | `if (instance != null) throw ...` | Prevents bypassing private constructors using Java Reflection (`setAccessible(true)`). |

---

## 4. How to Use This Design Pattern

### Client Code ([`Main.java`](file:///home/mohamedtoukhy/Java/DesginPattern/singleton-pattern/src/Main.java))
```java
public class Main {
    public static void main(String[] args) {
        // Call getInstance() from multiple places
        AppConfig a = AppConfig.getInstance();
        AppConfig b = AppConfig.getInstance();

        // Both references point to the exact same object in heap memory
        System.out.println("Same instance (a == b): " + (a == b)); // prints true
        System.out.println("Project Name:           " + a.getProjectName());
        System.out.println("Database URL:           " + a.getDbUrl());
    }
}
```

### How to Compile & Run
```bash
cd singleton-pattern
javac src/*.java
java -cp src Main
```

---

## 5. When to Use the Singleton Pattern

Use the Singleton Pattern when:
1. **Exactly one instance is required:** An entity manages a centralized system resource (hardware drivers, spoolers, loggers, caches).
2. **Global access without global variables:** You need clean, disciplined access across different application layers without passing dependencies through dozens of constructors.
3. **Lazy allocation is important:** The resource is heavy to construct, so it should only load if and when someone actually calls it.

---

## 6. Other Real-World Use Cases

* **Spring Framework Beans:** By default, every `@Component`, `@Service`, or `@Repository` registered in the Spring Application Context is a singleton bean (`@Scope("singleton")`).
* **Database Connection Pools:** `HikariDataSource` or `BasicDataSource` instances are managed as singletons to control physical TCP connections to PostgreSQL, MySQL, or Oracle.
* **Java Standard Library (JDK):**
  * `java.lang.Runtime.getRuntime()`: Single access to the JVM runtime process.
  * `java.awt.Desktop.getDesktop()`: Single access to native desktop integration.
  * `java.lang.System`: All console and environment operations are accessed through single static state.
* **Logging Frameworks:** `LogManager` in Logback, Log4j, and SLF4J maintains a singleton configuration registry to avoid re-reading log configurations per log event.
* **Hardware & Device Drivers:** Controlling a physical printer, serial port, or audio output device where multiple parallel handles would produce corrupted outputs.

