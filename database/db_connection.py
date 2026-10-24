import sqlite3

DB_NAME = "travel_agency_v2.db"

def get_connection():
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    return conn

def initialize_database():
    conn = get_connection()
    cursor = conn.cursor()
    
    # Users table
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS users (
        username TEXT PRIMARY KEY,
        password TEXT NOT NULL
    );
    """)

    # Packages table
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS packages (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        destination TEXT NOT NULL,
        price REAL NOT NULL,
        duration TEXT NOT NULL,
        itinerary TEXT NOT NULL
    );
    """)

    # Bookings table (Now linked to username)
    cursor.execute("""
    CREATE TABLE IF NOT EXISTS bookings (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        booking_id TEXT UNIQUE NOT NULL,
        username TEXT NOT NULL,
        package_id INTEGER NOT NULL,
        amount_due REAL NOT NULL,
        payment_status TEXT NOT NULL,
        FOREIGN KEY (package_id) REFERENCES packages(id),
        FOREIGN KEY (username) REFERENCES users(username)
    );
    """)

    conn.commit()
    conn.close()