import datetime
import time
from database.db_connection import initialize_database
from database.package_dao import PackageDAO
from database.booking_dao import BookingDAO

def main():
    initialize_database()
    PackageDAO.seed_data_if_empty()
    BookingDAO.seed_bookings_if_empty()

    while True:
        print("\n==================================================")
        print("       TRAVEL AGENCY MANAGEMENT SYSTEM (Python)   ")
        print("==================================================")
        print("--- Module 1: Travel Packages ---")
        print("1. Browse All Packages (SCRUM-3)")
        print("2. Search Packages by Destination (SCRUM-4)")
        print("3. Filter Packages by Maximum Budget (SCRUM-4)")
        print("4. View Package Details & Itinerary (SCRUM-5)")
        print("\n--- Module 2: Manage Payments ---")
        print("5. View All Customer Bookings (Overview)")
        print("6. Check Booking Amount Due (SCRUM-6)")
        print("7. Make Simulated Payment (SCRUM-7)")
        print("8. View Payment Status & Generate Receipt (SCRUM-8)")
        print("\n9. Exit System")
        
        choice = input("Select an option (1-9): ").strip()
        
        if choice == "9":
            print("Exiting system. Thank you for using Travel Agency Management System!")
            break

        elif choice == "1":
            packages = PackageDAO.get_all_packages()
            print("\n--- Available Travel Packages ---")
            for p in packages:
                p.display_summary()

        elif choice == "2":
            query = input("Enter destination to search: ").strip()
            results = PackageDAO.search_by_destination(query)
            if not results:
                print(f"No packages found matching '{query}'.")
            else:
                print(f"\n--- Search Results for '{query}' ---")
                for p in results:
                    p.display_summary()

        elif choice == "3":
            try:
                budget = float(input("Enter maximum budget (₹): ").strip())
                results = PackageDAO.filter_by_budget(budget)
                if not results:
                    print(f"No packages found within budget ₹{budget}")
                else:
                    print(f"\n--- Packages Within Budget ₹{budget} ---")
                    for p in results:
                        p.display_summary()
            except ValueError:
                print("Error: Invalid budget format. Please enter numeric digits.")

        elif choice == "4":
            try:
                pkg_id = int(input("Enter Package ID: ").strip())
                pkg = PackageDAO.get_package_by_id(pkg_id)
                if pkg:
                    pkg.display_details()
                else:
                    print(f"Error: Package ID {pkg_id} does not exist.")
            except ValueError:
                print("Error: Package ID must be a numeric integer.")

        elif choice == "5":
            bookings = BookingDAO.get_all_bookings()
            print("\n--- Existing Customer Bookings ---")
            for b in bookings:
                b.display_summary()

        elif choice == "6":
            ref_id = input("Enter Booking Reference ID (e.g., BK-101): ").strip()
            booking = BookingDAO.get_booking_by_id(ref_id)
            if booking:
                print("\n-------------------------------------------")
                print(f"Booking Reference : {booking.booking_id}")
                print(f"Booked Destination: {booking.destination}")
                print(f"Total Amount Due  : ₹{booking.amount_due}")
                print(f"Current Status    : {booking.payment_status}")
                print("-------------------------------------------")
            else:
                print(f"Error: Booking reference '{ref_id}' not found.")

        elif choice == "7":
            pay_id = input("Enter Booking Reference ID: ").strip()
            booking = BookingDAO.get_booking_by_id(pay_id)
            if not booking:
                print("Error: Booking reference not found.")
            elif booking.payment_status.lower() == "paid":
                print("Notice: This booking has already been fully paid.")
            else:
                print(f"Amount Due: ₹{booking.amount_due}")
                mode_opt = input("Payment Mode (1. UPI / 2. Credit Card / 3. Net Banking): ").strip()
                mode = "Credit Card" if mode_opt == "2" else ("Net Banking" if mode_opt == "3" else "UPI")
                
                try:
                    amount = float(input(f"Enter exact amount to pay (₹{booking.amount_due}): ").strip())
                    if amount == booking.amount_due:
                        print(f"Initiating payment via {mode}...")
                        time.sleep(1)
                        if BookingDAO.process_payment(booking.booking_id):
                            txn_id = f"TXN-{int(time.time() * 1000)}"
                            print(f">> Payment Successful! Transaction ID: {txn_id}")
                            print(">> Database updated: Booking status changed to 'Paid'.")
                        else:
                            print("Error: Database update failed.")
                    else:
                        print("Error: Payment amount does not match due amount.")
                except ValueError:
                    print("Error: Please enter a valid payment amount.")

        elif choice == "8":
            rec_id = input("Enter Booking Reference ID: ").strip()
            booking = BookingDAO.get_booking_by_id(rec_id)
            if not booking:
                print("Error: Booking reference not found.")
            else:
                print("\n===========================================")
                print("         OFFICIAL PAYMENT RECEIPT          ")
                print("===========================================")
                print(f"Booking Reference : {booking.booking_id}")
                print(f"Destination       : {booking.destination}")
                print(f"Total Amount      : ₹{booking.amount_due}")
                print(f"Payment Status    : {booking.payment_status}")
                print(f"Receipt Generated : {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
                print("===========================================")

        else:
            print("Invalid option. Please enter 1 to 9.")

if __name__ == "__main__":
    main()