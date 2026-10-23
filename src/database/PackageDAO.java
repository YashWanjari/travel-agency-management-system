package database;

import model.Package;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageDAO {

    public static void seedDataIfEmpty() {
        String countSql = "SELECT COUNT(*) FROM packages;";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO packages (destination, price, duration, itinerary) VALUES " +
                    "('Goa Beach Escape', 12000.0, '3 Days / 2 Nights', 'Day 1: North Goa beaches\nDay 2: Water sports at Baga\nDay 3: Sunset cruise and departure'), " +
                    "('Manali Mountain Retreat', 18500.0, '4 Days / 3 Nights', 'Day 1: Solang valley sightseeing\nDay 2: Rohtang pass snow trek\nDay 3: Old Manali cafes\nDay 4: Departure'), " +
                    "('Kerala Backwaters Tour', 24000.0, '5 Days / 4 Nights', 'Day 1: Munnar tea gardens\nDay 2: Alleppey houseboat cruise\nDay 3: Kumarakom bird sanctuary\nDay 4-5: Cochin heritage walk'), " +
                    "('Jaipur Royal Heritage', 9500.0, '2 Days / 1 Night', 'Day 1: Amer Fort & Hawa Mahal\nDay 2: City Palace & local bazaar shopping');";
                stmt.executeUpdate(insertSql);
                System.out.println("Initial package data seeded successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Seeding error: " + e.getMessage());
        }
    }

    public static List<Package> getAllPackages() {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM packages;";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Package(
                    rs.getInt("id"),
                    rs.getString("destination"),
                    rs.getDouble("price"),
                    rs.getString("duration"),
                    rs.getString("itinerary")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fetch error: " + e.getMessage());
        }
        return list;
    }

    public static List<Package> searchByDestination(String keyword) {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM packages WHERE LOWER(destination) LIKE ?;";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword.toLowerCase() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Package(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        rs.getDouble("price"),
                        rs.getString("duration"),
                        rs.getString("itinerary")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
        return list;
    }

    public static List<Package> filterByBudget(double maxBudget) {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM packages WHERE price <= ? ORDER BY price ASC;";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, maxBudget);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Package(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        rs.getDouble("price"),
                        rs.getString("duration"),
                        rs.getString("itinerary")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Filter error: " + e.getMessage());
        }
        return list;
    }

    public static Package getPackageById(int id) {
        String sql = "SELECT * FROM packages WHERE id = ?;";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Package(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        rs.getDouble("price"),
                        rs.getString("duration"),
                        rs.getString("itinerary")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lookup error: " + e.getMessage());
        }
        return null;
    }
}