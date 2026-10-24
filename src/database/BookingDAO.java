package database;

import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public static void seedBookingsIfEmpty() {
        String countSql = "SELECT COUNT(*) FROM bookings;";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO bookings (booking_id, package_id, amount_due, payment_status) VALUES " +
                        "('BK-101', 1, 12000.0, 'Pending'), " +
                        "('BK-102', 2, 18500.0, 'Pending'), " +
                        "('BK-103', 4, 9500.0, 'Paid');";
                stmt.executeUpdate(insertSql);
                System.out.println("Sample booking records initialized successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Booking seeding error: " + e.getMessage());
        }
    }

    public static Booking getBookingById(String bookingId) {
        String sql = "SELECT b.booking_id, b.package_id, p.destination, b.amount_due, b.payment_status " +
                     "FROM bookings b " +
                     "JOIN packages p ON b.package_id = p.id " +
                     "WHERE LOWER(b.booking_id) = ?;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingId.toLowerCase().trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Booking(
                        rs.getString("booking_id"),
                        rs.getInt("package_id"),
                        rs.getString("destination"),
                        rs.getDouble("amount_due"),
                        rs.getString("payment_status")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Booking lookup error: " + e.getMessage());
        }
        return null;
    }

    public static List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.package_id, p.destination, b.amount_due, b.payment_status " +
                     "FROM bookings b " +
                     "JOIN packages p ON b.package_id = p.id;";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Booking(
                    rs.getString("booking_id"),
                    rs.getInt("package_id"),
                    rs.getString("destination"),
                    rs.getDouble("amount_due"),
                    rs.getString("payment_status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Fetch bookings error: " + e.getMessage());
        }
        return list;
    }

    public static boolean processPayment(String bookingId, double paidAmount) {
        String updateSql = "UPDATE bookings SET payment_status = 'Paid' WHERE LOWER(booking_id) = ?;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

            pstmt.setString(1, bookingId.toLowerCase().trim());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Payment processing error: " + e.getMessage());
            return false;
        }
    }
}