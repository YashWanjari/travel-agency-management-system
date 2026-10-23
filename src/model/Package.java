package model;

public class Package {
    private int id;
    private String destination;
    private double price;
    private String duration;
    private String itinerary;

    public Package(int id, String destination, double price, String duration, String itinerary) {
        this.id = id;
        this.destination = destination;
        this.price = price;
        this.duration = duration;
        this.itinerary = itinerary;
    }

    public int getId() { return id; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }
    public String getDuration() { return duration; }
    public String getItinerary() { return itinerary; }

    public void displaySummary() {
        System.out.println("ID: " + id + " | " + destination + " | ₹" + price + " | Duration: " + duration);
    }

    public void displayDetails() {
        System.out.println("\n-------------------------------------------");
        System.out.println("Package ID: " + id);
        System.out.println("Destination: " + destination);
        System.out.println("Price: ₹" + price);
        System.out.println("Duration: " + duration);
        System.out.println("Detailed Itinerary:\n" + itinerary);
        System.out.println("-------------------------------------------");
    }
}