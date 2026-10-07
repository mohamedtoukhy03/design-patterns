public class Main {
    static void main() {
        AppConfig a = AppConfig.getInstance();
        AppConfig b = AppConfig.getInstance();
        System.out.println(a == b);              // true: same object
        System.out.println(a.getDbUrl());        // jdbc:h2:mem:testdb
    }
}
