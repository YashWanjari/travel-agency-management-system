from flask import Flask, render_template, request, jsonify, session, redirect, url_for
from database.db_connection import initialize_database, get_connection
from database.package_dao import PackageDAO
from database.booking_dao import BookingDAO
import time
import datetime

app = Flask(__name__)
app.secret_key = "super_secret_travel_key" # Required for login sessions

# Setup DB
initialize_database()
PackageDAO.seed_data_if_empty()

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form.get('username').strip()
        password = request.form.get('password').strip()
        
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute("SELECT * FROM users WHERE username = ?", (username,))
        user = cursor.fetchone()
        
        if not user:
            # First time? Auto-create account
            cursor.execute("INSERT INTO users (username, password) VALUES (?, ?)", (username, password))
            conn.commit()
        elif user['password'] != password:
            return render_template('login.html', error="Wrong password!")
            
        conn.close()
        session['username'] = username
        return redirect(url_for('home'))
        
    return render_template('login.html')

@app.route('/logout')
def logout():
    session.pop('username', None)
    return redirect(url_for('login'))

@app.route('/')
def home():
    if 'username' not in session:
        return redirect(url_for('login'))
        
    packages = PackageDAO.get_all_packages()
    bookings = BookingDAO.get_bookings_by_user(session['username'])
    return render_template('index.html', packages=packages, bookings=bookings, username=session['username'])

@app.route('/api/packages/search', methods=['GET'])
def search_packages():
    query = request.args.get('q', '').strip()
    max_budget = request.args.get('budget', type=float)
    if query: packages = PackageDAO.search_by_destination(query)
    elif max_budget: packages = PackageDAO.filter_by_budget(max_budget)
    else: packages = PackageDAO.get_all_packages()
        
    return jsonify([{'id': p.id, 'destination': p.destination, 'price': p.price, 'duration': p.duration, 'itinerary': p.itinerary} for p in packages])

@app.route('/api/bookings/book', methods=['POST'])
def book_package():
    if 'username' not in session: return jsonify({'success': False, 'message': 'Please login first'}), 401
    
    data = request.json
    pkg = PackageDAO.get_package_by_id(data.get('package_id'))
    if not pkg: return jsonify({'success': False, 'message': 'Package not found'}), 404
        
    booking_id = BookingDAO.create_booking(session['username'], pkg.id, pkg.price)
    return jsonify({'success': True, 'booking_id': booking_id})

@app.route('/api/bookings/lookup', methods=['GET'])
def lookup_booking():
    if 'username' not in session: return jsonify({'success': False}), 401
    booking_id = request.args.get('id', '').strip()
    booking = BookingDAO.get_booking_by_id_and_user(booking_id, session['username'])
    if booking:
        return jsonify({'success': True, 'booking_id': booking.booking_id, 'destination': booking.destination, 'amount_due': booking.amount_due, 'payment_status': booking.payment_status})
    return jsonify({'success': False, 'message': 'Booking not found or belongs to another user.'}), 404

@app.route('/api/bookings/pay', methods=['POST'])
def process_payment():
    data = request.json or {}
    booking_id = data.get('booking_id', '').strip()
    amount = float(data.get('amount', 0))

    booking = BookingDAO.get_booking_by_id_and_user(booking_id, session['username'])
    if not booking: return jsonify({'success': False, 'message': 'Invalid booking'}), 404
    if booking.payment_status.lower() == 'paid': return jsonify({'success': False, 'message': 'Already paid'}), 400
    if amount != booking.amount_due: return jsonify({'success': False, 'message': 'Incorrect amount'}), 400

    if BookingDAO.process_payment(booking_id):
        return jsonify({'success': True, 'txn_id': f"TXN-{int(time.time() * 1000)}", 'timestamp': datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')})
    return jsonify({'success': False}), 500

if __name__ == '__main__':
    app.run(debug=True, port=5000)