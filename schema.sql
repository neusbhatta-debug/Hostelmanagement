CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    room_no TEXT NOT NULL UNIQUE,
    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    roll TEXT NOT NULL UNIQUE,
    contact TEXT NOT NULL,
    room_id INTEGER,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE IF NOT EXISTS fees (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE IF NOT EXISTS maintenance_requests (
    request_id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id)
);

INSERT OR IGNORE INTO users(username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
INSERT OR IGNORE INTO users(username, password, role) VALUES ('sita', 'sita123', 'STUDENT');
INSERT OR IGNORE INTO users(username, password, role) VALUES ('ram', 'ram123', 'STUDENT');

INSERT OR IGNORE INTO rooms(id, room_no, status) VALUES (1, 'A-101', 'OCCUPIED');
INSERT OR IGNORE INTO rooms(id, room_no, status) VALUES (2, 'A-102', 'OCCUPIED');
INSERT OR IGNORE INTO rooms(id, room_no, status) VALUES (3, 'B-201', 'AVAILABLE');
INSERT OR IGNORE INTO rooms(id, room_no, status) VALUES (4, 'B-202', 'AVAILABLE');

INSERT OR IGNORE INTO students(id, name, roll, contact, room_id, username, password)
VALUES (1, 'Sita Sharma', 'PCPS-001', '9800000001', 1, 'sita', 'sita123');

INSERT OR IGNORE INTO students(id, name, roll, contact, room_id, username, password)
VALUES (2, 'Ram Thapa', 'PCPS-002', '9800000002', 2, 'ram', 'ram123');

INSERT OR IGNORE INTO fees(id, student_id, amount, status) VALUES (1, 1, 12000, 'PAID');
INSERT OR IGNORE INTO fees(id, student_id, amount, status) VALUES (2, 2, 12000, 'UNPAID');

INSERT OR IGNORE INTO maintenance_requests(request_id, student_id, description, status)
VALUES (1, 1, 'Fan is not working', 'PENDING');

INSERT OR IGNORE INTO maintenance_requests(request_id, student_id, description, status)
VALUES (2, 2, 'Bathroom tap is leaking', 'IN_PROGRESS');
