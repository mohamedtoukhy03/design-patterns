public class Main {
    public static void main(String[] args) {
        AppConfig a = AppConfig.getInstance();
        AppConfig b = AppConfig.getInstance();

        System.out.println("Same instance (a == b): " + (a == b)); // true
        System.out.println("Project Name:           " + a.getProjectName());
        System.out.println("Database URL:           " + a.getDbUrl());
    }
}
