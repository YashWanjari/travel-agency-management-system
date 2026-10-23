import database.DBConnection;
import database.PackageDAO;
import model.Package;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBConnection.initializeDatabase();
        PackageDAO.seedDataIfEmpty();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Travel Agency Management System (Module 1) ===");
            System.out.println("1. Browse All Packages (SCRUM-3)");
            System.out.println("2. Search Packages by Destination (SCRUM-4)");
            System.out.println("3. Filter Packages by Maximum Budget (SCRUM-4)");
            System.out.println("4. View Package Details and Itinerary (SCRUM-5)");
            System.out.println("5. Exit");
            System.out.print("Enter choice (1-5): ");

            String input = scanner.nextLine().trim();
            if (input.equals("5")) {
                System.out.println("Exiting system. Goodbye!");
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

                default:
                    System.out.println("Invalid selection. Please choose an option between 1 and 5.");
            }
        }
        scanner.close();
    }
}