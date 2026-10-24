package model;

public class Booking {
    private String bookingId;
    private int packageId;
    private String destination;
    private double amountDue;
    private String paymentStatus;

    public Booking(String bookingId, int packageId, String destination, double amountDue, String paymentStatus) {
        this.bookingId = bookingId;
        this.packageId = packageId;
        this.destination = destination;
        this.amountDue = amountDue;
        this.paymentStatus = paymentStatus;
    }

    public String getBookingId() { return bookingId; }
    public int getPackageId() { return packageId; }
    public String getDestination() { return destination; }
    public double getAmountDue() { return amountDue; }
    public String getPaymentStatus() { return paymentStatus; }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void displayBookingSummary() {
        System.out.println("Booking Reference: " + bookingId +
                " | Package: " + destination +
                " | Amount Due: ₹" + amountDue +
                " | Status: " + paymentStatus);
    }
}