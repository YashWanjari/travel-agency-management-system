# Travel Agency Management System
Software Engineering PBL Project (Scrum Model)

## Implemented Modules:
1. View Travel Packages (SCRUM-3, SCRUM-4, SCRUM-5)
2. Manage Payments (SCRUM-6, SCRUM-7, SCRUM-8)

## Sprint Tracking:
- Jira Project Key: SCRUM
- Sprint 1: Oct 20, 2026 - Oct 27, 2026


## Day 2 Progress (21 Oct 2026):
- Documented detailed user stories and acceptance criteria for all 6 backlog items.
- Assigned priorities (Highest, High, Medium) mapped to Module 1 and Module 2.
- Assigned all Sprint 1 stories to the developer.
- Requirements baseline established in Jira. 

## Day 3 Progress (22 Oct 2026):
- Decomposed all 6 user stories into granular technical subtasks in Jira.
- Established Java project structure (src, database, model, lib).
- Integrated SQLite JDBC standalone driver and established database connectivity.
- Implemented schema initialization for packages and bookings tables. 

## Day 4 Progress (23 Oct 2026):
- Completed Module 1 (View Travel Packages): SCRUM-3, SCRUM-4, SCRUM-5.
- Implemented Package model and PackageDAO data access layer.
- Seeded travel packages dataset into SQLite database.
- Implemented console-based interactive browsing, destination search, and budget filtering logic.
- Moved SCRUM-3, SCRUM-4, and SCRUM-5 stories to Done status in Jira. 


## Day 5 Progress (24 Oct 2026):
- Completed Module 2 (Manage Payments): SCRUM-6, SCRUM-7, SCRUM-8.
- Implemented Booking model and BookingDAO data access layer.
- Added seed customer bookings into SQLite database.
- Implemented booking balance inquiry, simulated multi-mode payment gateway, and receipt generator.
- Verified database state transitions from 'Pending' to 'Paid' upon successful transaction.
- Moved SCRUM-6, SCRUM-7, and SCRUM-8 (including subtasks) to Done status in Jira. 


## Day 6: Sprint 1 Closure & Testing (25 Oct 2026)

### 1. Quality Assurance (Testing)
- **Black Box Testing:** Conducted manual UI testing for User Registration, Package filtering (keyword/budget), Booking generation, and Payment simulation. All test cases passed.
- **White Box Testing:** Automated unit tests implemented in `test_app.py` using Python's `unittest` framework to verify DAO logic, database insertions, and user-isolated query execution.

### 2. Sprint 1 Review
- **Goal Achieved:** Successfully delivered a functional prototype allowing users to browse packages, make bookings, and simulate payments.
- **Scope:** Completed User Stories SCRUM-3 to SCRUM-8.
- **Deliverables:** A full-stack web application with SQLite database persistence and isolated user sessions.

### 3. Sprint 1 Retrospective
- **What went well:** The team successfully adapted to a tech-stack pivot (Java CLI to Python/Flask Web App) to improve scalability, maintainability, and user experience. Zero-dependency database setup (SQLite) enabled rapid prototyping.
- **What could be improved:** Initial database schema lacked user isolation, requiring a mid-sprint database migration. 
- **Action items for Sprint 2:** Plan entity relationships (ER schema) more thoroughly before writing code. Implement a robust Admin Panel for inventory management.


## Day 7: Integration Testing & Payment Edge-Case Verification (26 Oct 2026)

### 1. Integration & System Testing (`test_integration.py`)
Conducted automated integration testing against the Flask application routes and SQLite persistent layer using Python's `unittest` and Flask's `test_client`.

- **Boundary Condition Testing:** Verified that looking up non-existent booking IDs returns an explicit `404 Not Found` response with appropriate error handling instead of uncaught exceptions.
- **Edge-Case Validation (Payment Mismatch):** Tested payment gateway simulation with mismatched transaction amounts. Attempting to pay an amount differing from `amount_due` is strictly rejected (`400 Bad Request`) to prevent data corruption.
- **End-to-End Payment Flow:** Verified that submitting the exact due amount transitions the booking status from `Pending` to `Paid`, correctly generates a persistent Transaction ID (`TXN-XXXXXX`), and updates the database records.

### 2. Test Execution Results
All automated integration tests executed successfully:
- Total tests executed: 3
- Failures / Errors: 0
- Status: **PASSED (OK)**

### 3. Jira Task Updates
- **SCRUM-7 (Make a simulated payment):** Completed integration verification and moved to **Done**.
- **SCRUM-8 (View payment status and receipt):** Moved to **In Review** pending final sprint acceptance.