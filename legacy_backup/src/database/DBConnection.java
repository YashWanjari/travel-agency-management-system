package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static final String URL = "jdbc:sqlite:travel_agency.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found on classpath!");
        }
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createPackagesTable = "CREATE TABLE IF NOT EXISTS packages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "destination TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "duration TEXT NOT NULL, " +
                "itinerary TEXT NOT NULL);";

        String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id TEXT PRIMARY KEY, " +
                "package_id INTEGER, " +
                "amount_due REAL NOT NULL, " +
                "payment_status TEXT NOT NULL, " +
                "FOREIGN KEY(package_id) REFERENCES packages(id));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createPackagesTable);
            stmt.execute(createBookingsTable);
            System.out.println("SQLite database and tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}