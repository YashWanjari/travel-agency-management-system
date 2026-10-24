from database.db_connection import get_connection
from models.booking import Booking
import random

class BookingDAO:
    @staticmethod
    def get_bookings_by_user(username):
        conn = get_connection()
        cursor = conn.cursor()
        sql = """
        SELECT b.booking_id, b.package_id, p.destination, b.amount_due, b.payment_status 
        FROM bookings b 
        JOIN packages p ON b.package_id = p.id
        WHERE b.username = ?;
        """
        cursor.execute(sql, (username,))
        rows = cursor.fetchall()
        conn.close()
        return [Booking(r['booking_id'], r['package_id'], r['destination'], r['amount_due'], r['payment_status']) for r in rows]

    @staticmethod
    def get_booking_by_id_and_user(booking_id, username):
        conn = get_connection()
        cursor = conn.cursor()
        sql = """
        SELECT b.booking_id, b.package_id, p.destination, b.amount_due, b.payment_status 
        FROM bookings b 
        JOIN packages p ON b.package_id = p.id 
        WHERE LOWER(b.booking_id) = ? AND b.username = ?;
        """
        cursor.execute(sql, (booking_id.lower().strip(), username))
        row = cursor.fetchone()
        conn.close()
        if row:
            return Booking(row['booking_id'], row['package_id'], row['destination'], row['amount_due'], row['payment_status'])
        return None

    @staticmethod
    def process_payment(booking_id):
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("UPDATE bookings SET payment_status = 'Paid' WHERE LOWER(booking_id) = ?;", (booking_id.lower().strip(),))
        conn.commit()
        rows_affected = cursor.rowcount
        conn.close()
        return rows_affected > 0

    @staticmethod
    def create_booking(username, package_id, amount):
        conn = get_connection()
        cursor = conn.cursor()
        booking_id = f"BK-{random.randint(1000, 9999)}"
        cursor.execute("INSERT INTO bookings (booking_id, username, package_id, amount_due, payment_status) VALUES (?, ?, ?, ?, 'Pending');", (booking_id, username, package_id, amount))
        conn.commit()
        conn.close()
        return booking_id