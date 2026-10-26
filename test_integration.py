import unittest
import json
from app import app
from database.db_connection import initialize_database
from database.package_dao import PackageDAO
from database.booking_dao import BookingDAO

class TestTravelAgencyIntegration(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        app.config['TESTING'] = True
        cls.client = app.test_client()
        initialize_database()
        PackageDAO.seed_data_if_empty()

    def test_edge_case_invalid_booking_lookup(self):
        """Boundary test: Lookup with non-existent booking ID should fail gracefully"""
        with self.client.session_transaction() as sess:
            sess['username'] = 'qa_tester'
            
        res = self.client.get('/api/bookings/lookup?id=NON_EXISTENT_999')
        self.assertEqual(res.status_code, 404)
        data = json.loads(res.data)
        self.assertFalse(data['success'])

    def test_edge_case_payment_amount_mismatch(self):
        """Edge-case test: Processing payment with mismatched amount must be rejected"""
        with self.client.session_transaction() as sess:
            sess['username'] = 'qa_tester'

        # Create dummy booking
        booking_id = BookingDAO.create_booking('qa_tester', 1, 12000.0)

        # Attempt payment with incorrect amount (10000 instead of 12000)
        res = self.client.post('/api/bookings/pay', 
                               data=json.dumps({'booking_id': booking_id, 'amount': 10000.0}),
                               content_type='application/json')
        self.assertEqual(res.status_code, 400)
        data = json.loads(res.data)
        self.assertFalse(data['success'])
        self.assertIn("Incorrect amount", data['message'])

    def test_successful_payment_flow(self):
        """Integration test: Correct amount transitions status to Paid"""
        with self.client.session_transaction() as sess:
            sess['username'] = 'qa_tester'

        booking_id = BookingDAO.create_booking('qa_tester', 2, 18500.0)

        res = self.client.post('/api/bookings/pay',
                               data=json.dumps({'booking_id': booking_id, 'amount': 18500.0}),
                               content_type='application/json')
        self.assertEqual(res.status_code, 200)
        data = json.loads(res.data)
        self.assertTrue(data['success'])
        self.assertTrue(data['txn_id'].startswith("TXN-"))

if __name__ == '__main__':
    unittest.main()
    