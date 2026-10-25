import unittest
import os
from database.db_connection import initialize_database
from database.package_dao import PackageDAO
from database.booking_dao import BookingDAO

class TestTravelAgency(unittest.TestCase):
    
    @classmethod
    def setUpClass(cls):
        # Initialize test database environment
        initialize_database()
        PackageDAO.seed_data_if_empty()

    def test_1_search_package(self):
        results = PackageDAO.search_by_destination("Goa")
        self.assertTrue(len(results) > 0, "Goa package should exist in DB")
        self.assertEqual(results[0].destination, 'Goa Beach Escape')

    def test_2_budget_filter(self):
        results = PackageDAO.filter_by_budget(10000)
        for pkg in results:
            self.assertTrue(pkg.price <= 10000, "Price should be strictly <= 10000")

    def test_3_create_and_fetch_booking(self):
        test_user = "test_yash"
        pkg_id = 1
        amount = 12000.0
        
        # Test booking creation
        booking_id = BookingDAO.create_booking(test_user, pkg_id, amount)
        self.assertTrue(booking_id.startswith("BK-"), "Booking ID should follow BK- format")
        
        # Test user isolation (fetching only for this user)
        booking = BookingDAO.get_booking_by_id_and_user(booking_id, test_user)
        self.assertIsNotNone(booking, "Booking should exist for this user")
        self.assertEqual(booking.payment_status, "Pending")

if __name__ == '__main__':
    unittest.main()