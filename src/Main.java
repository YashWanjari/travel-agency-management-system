import database.DBConnection;
import database.PackageDAO;
import database.BookingDAO;
import model.Package;
import model.Booking;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBConnection.initializeDatabase();
        PackageDAO.seedDataIfEmpty();
        BookingDAO.seedBookingsIfEmpty();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("       TRAVEL AGENCY MANAGEMENT SYSTEM            ");
            System.out.println("==================================================");
            System.out.println("--- Module 1: Travel Packages ---");
            System.out.println("1. Browse All Packages (SCRUM-3)");
            System.out.println("2. Search Packages by Destination (SCRUM-4)");
            System.out.println("3. Filter Packages by Maximum Budget (SCRUM-4)");
            System.out.println("4. View Package Details and Itinerary (SCRUM-5)");
            System.out.println("\n--- Module 2: Manage Payments ---");
            System.out.println("5. View All Bookings (Overview)");
            System.out.println("6. Check Booking Amount Due (SCRUM-6)");
            System.out.println("7. Make Simulated Payment (SCRUM-7)");
            System.out.println("8. View Payment Status & Generate Receipt (SCRUM-8)");
            System.out.println("\n9. Exit System");
            System.out.print("Select an option (1-9): ");

            String input = scanner.nextLine().trim();
            if (input.equals("9")) {
                System.out.println("Exiting Travel Agency Management System. Goodbye!");
                break;
            }

            switch (input) {
                case "1":
                    List<Package> packages = PackageDAO.getAllPackages();
                    if (packages.isEmpty()) {
                        System.out.println("No packages available currently.");
                    } else {
                        System.out.println("\n--- Available Packages ---");
                        for (Package p : packages) {
                            p.displaySummary();
                        }
                    }
                    break;

                case "2":
                    System.out.print("Enter destination keyword to search: ");
                    String query = scanner.nextLine().trim();
                    List<Package> searchResults = PackageDAO.searchByDestination(query);
                    if (searchResults.isEmpty()) {
                        System.out.println("No travel packages found matching '" + query + "'.");
                    } else {
                        System.out.println("\n--- Search Results ---");
                        for (Package p : searchResults) {
                            p.displaySummary();
                        }
                    }
                    break;

                case "3":
                    System.out.print("Enter your maximum budget (₹): ");
                    try {
                        double budget = Double.parseDouble(scanner.nextLine().trim());
                        List<Package> budgetResults = PackageDAO.filterByBudget(budget);
                        if (budgetResults.isEmpty()) {
                            System.out.println("No packages found within budget ₹" + budget);
                        } else {
                            System.out.println("\n--- Packages Within Budget ₹" + budget + " ---");
                            for (Package p : budgetResults) {
                                p.displaySummary();
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount entered. Please enter a valid number.");
                    }
                    break;

                case "4":
                    System.out.print("Enter Package ID to view details: ");
                    try {
                        int pkgId = Integer.parseInt(scanner.nextLine().trim());
                        Package selectedPkg = PackageDAO.getPackageById(pkgId);
                        if (selectedPkg != null) {
                            selectedPkg.displayDetails();
                        } else {
                            System.out.println("Error: Package with ID " + pkgId + " does not exist.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Package ID. Please enter a numeric ID.");
                    }
                    break;

                case "5":
                    List<Booking> bookings = BookingDAO.getAllBookings();
                    System.out.println("\n--- Existing Customer Bookings ---");
                    for (Booking b : bookings) {
                        b.displayBookingSummary();
                    }
                    break;

                case "6":
                    System.out.print("Enter Booking Reference ID (e.g., BK-101): ");
                    String refId = scanner.nextLine().trim();
                    Booking booking = BookingDAO.getBookingById(refId);
                    if (booking != null) {
                        System.out.println("\n-------------------------------------------");
                        System.out.println("Booking Reference : " + booking.getBookingId());
                        System.out.println("Booked Destination: " + booking.getDestination());
                        System.out.println("Total Amount Due  : ₹" + booking.getAmountDue());
                        System.out.println("Current Status    : " + booking.getPaymentStatus());
                        System.out.println("-------------------------------------------");
                    } else {
                        System.out.println("Error: Booking reference '" + refId + "' not found.");
                    }
                    break;

                case "7":
                    System.out.print("Enter Booking Reference ID for Payment: ");
                    String payBookingId = scanner.nextLine().trim();
                    Booking payBooking = BookingDAO.getBookingById(payBookingId);

                    if (payBooking == null) {
                        System.out.println("Error: Booking reference not found.");
                    } else if ("Paid".equalsIgnoreCase(payBooking.getPaymentStatus())) {
                        System.out.println("Notice: This booking has already been fully paid.");
                    } else {
                        System.out.println("Total Amount Due: ₹" + payBooking.getAmountDue());
                        System.out.print("Choose Payment Mode (1. UPI / 2. Credit Card / 3. Net Banking): ");
                        String modeChoice = scanner.nextLine().trim();
                        String mode = modeChoice.equals("2") ? "Credit Card" : (modeChoice.equals("3") ? "Net Banking" : "UPI");

                        System.out.print("Enter amount to pay (₹" + payBooking.getAmountDue() + "): ");
                        try {
                            double amount = Double.parseDouble(scanner.nextLine().trim());
                            if (amount == payBooking.getAmountDue()) {
                                System.out.println("Processing simulated payment via " + mode + "...");
                                boolean success = BookingDAO.processPayment(payBooking.getBookingId(), amount);
                                if (success) {
                                    System.out.println(">> Payment Successful! Transaction ID: TXN-" + System.currentTimeMillis());
                                    System.out.println(">> Database updated: Booking status changed to 'Paid'.");
                                } else {
                                    System.out.println("Error: Payment processing failed in database.");
                                }
                            } else {
                                System.out.println("Error: Incorrect payment amount. Exact due amount must be provided.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid payment amount entered.");
                        }
                    }
                    break;

                case "8":
                    System.out.print("Enter Booking Reference ID to print receipt: ");
                    String receiptId = scanner.nextLine().trim();
                    Booking receiptBooking = BookingDAO.getBookingById(receiptId);

                    if (receiptBooking == null) {
                        System.out.println("Error: Booking reference not found.");
                    } else {
                        System.out.println("\n===========================================");
                        System.out.println("         OFFICIAL PAYMENT RECEIPT          ");
                        System.out.println("===========================================");
                        System.out.println("Booking Reference : " + receiptBooking.getBookingId());
                        System.out.println("Destination       : " + receiptBooking.getDestination());
                        System.out.println("Total Amount      : ₹" + receiptBooking.getAmountDue());
                        System.out.println("Payment Status    : " + receiptBooking.getPaymentStatus());
                        System.out.println("Receipt Generated : " + new java.util.Date());
                        System.out.println("===========================================");
                    }
                    break;

                default:
                    System.out.println("Invalid selection. Please choose an option between 1 and 9.");
            }
        }
        scanner.close();
    }
}