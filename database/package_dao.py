from database.db_connection import get_connection
from models.package import Package

class PackageDAO:
    @staticmethod
    def seed_data_if_empty():
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT COUNT(*) FROM packages;")
        count = cursor.fetchone()[0]
        
        if count == 0:
            sample_packages = [
                ('Goa Beach Escape', 12000.0, '3 Days / 2 Nights', 'Day 1: North Goa beaches\nDay 2: Water sports at Baga\nDay 3: Sunset cruise and departure'),
                ('Manali Mountain Retreat', 18500.0, '4 Days / 3 Nights', 'Day 1: Solang valley sightseeing\nDay 2: Rohtang pass snow trek\nDay 3: Old Manali cafes\nDay 4: Departure'),
                ('Kerala Backwaters Tour', 24000.0, '5 Days / 4 Nights', 'Day 1: Munnar tea gardens\nDay 2: Alleppey houseboat cruise\nDay 3: Kumarakom bird sanctuary\nDay 4-5: Cochin heritage walk'),
                ('Jaipur Royal Heritage', 9500.0, '2 Days / 1 Night', 'Day 1: Amer Fort & Hawa Mahal\nDay 2: City Palace & local bazaar shopping')
            ]
            cursor.executemany("INSERT INTO packages (destination, price, duration, itinerary) VALUES (?, ?, ?, ?);", sample_packages)
            conn.commit()
            print("Initial packages seeded successfully.")
        conn.close()

    @staticmethod
    def get_all_packages():
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM packages;")
        rows = cursor.fetchall()
        conn.close()
        return [Package(r['id'], r['destination'], r['price'], r['duration'], r['itinerary']) for r in rows]

    @staticmethod
    def search_by_destination(keyword):
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM packages WHERE LOWER(destination) LIKE ?;", (f"%{keyword.lower().strip()}%",))
        rows = cursor.fetchall()
        conn.close()
        return [Package(r['id'], r['destination'], r['price'], r['duration'], r['itinerary']) for r in rows]

    @staticmethod
    def filter_by_budget(max_budget):
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM packages WHERE price <= ? ORDER BY price ASC;", (max_budget,))
        rows = cursor.fetchall()
        conn.close()
        return [Package(r['id'], r['destination'], r['price'], r['duration'], r['itinerary']) for r in rows]

    @staticmethod
    def get_package_by_id(pkg_id):
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM packages WHERE id = ?;", (pkg_id,))
        row = cursor.fetchone()
        conn.close()
        if row:
            return Package(row['id'], row['destination'], row['price'], row['duration'], row['itinerary'])
        return None