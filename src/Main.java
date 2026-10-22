import database.DBConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Travel Agency Management System...");
        DBConnection.initializeDatabase();
    }
}