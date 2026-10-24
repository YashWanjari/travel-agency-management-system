class Booking:
    def __init__(self, booking_id, package_id, destination, amount_due, payment_status):
        self.booking_id = booking_id
        self.package_id = package_id
        self.destination = destination
        self.amount_due = amount_due
        self.payment_status = payment_status

    def display_summary(self):
        print(f"Booking Ref: {self.booking_id} | Package: {self.destination} | Amount Due: ₹{self.amount_due} | Status: {self.payment_status}")