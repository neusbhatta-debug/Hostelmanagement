package hostelmanagement.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        createTables();
        insertDummyData();
    }

    private static void createTables() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)";

        String roomsTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "room_no TEXT NOT NULL UNIQUE, " +
                "status TEXT NOT NULL)";

        String studentsTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "roll TEXT NOT NULL UNIQUE, " +
                "contact TEXT NOT NULL, " +
                "room_id INTEGER, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "FOREIGN KEY (room_id) REFERENCES rooms(id), " +
                "FOREIGN KEY (username) REFERENCES users(username))";

        String feesTable = "CREATE TABLE IF NOT EXISTS fees (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "amount REAL NOT NULL, " +
                "status TEXT NOT NULL, " +
                "FOREIGN KEY (student_id) REFERENCES students(id))";

        String maintenanceTable = "CREATE TABLE IF NOT EXISTS maintenance_requests (" +
                "request_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "description TEXT NOT NULL, " +
                "status TEXT NOT NULL, " +
                "FOREIGN KEY (student_id) REFERENCES students(id))";

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(usersTable);
            statement.execute(roomsTable);
            statement.execute(studentsTable);
            statement.execute(feesTable);
            statement.execute(maintenanceTable);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void insertDummyData() {
        insertUser("admin", "admin123", "ADMIN");
        insertRoom("A-101", "OCCUPIED");
        insertRoom("A-102", "OCCUPIED");
        insertRoom("B-201", "AVAILABLE");
        insertRoom("B-202", "AVAILABLE");

        insertStudent("Sita Sharma", "PCPS-001", "9800000001", 1, "sita", "sita123");
        insertStudent("Ram Thapa", "PCPS-002", "9800000002", 2, "ram", "ram123");

        insertFee(1, 12000.0, "PAID");
        insertFee(2, 12000.0, "UNPAID");

        insertRequest(1, "Fan is not working", "PENDING");
        insertRequest(2, "Bathroom tap is leaking", "IN_PROGRESS");
    }

    private static void insertUser(String username, String password, String role) {
        String sql = "INSERT OR IGNORE INTO users(username, password, role) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void insertRoom(String roomNo, String status) {
        String sql = "INSERT OR IGNORE INTO rooms(room_no, status) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, roomNo);
            statement.setString(2, status);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void insertStudent(String name, String roll, String contact, Integer roomId, String username, String password) {
        insertUser(username, password, "STUDENT");
        String sql = "INSERT OR IGNORE INTO students(name, roll, contact, room_id, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, roll);
            statement.setString(3, contact);
            if (roomId == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, roomId);
            }
            statement.setString(5, username);
            statement.setString(6, password);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void insertFee(int studentId, double amount, String status) {
        String sql = "INSERT OR IGNORE INTO fees(id, student_id, amount, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, studentId);
            statement.setDouble(3, amount);
            statement.setString(4, status);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private static void insertRequest(int studentId, String description, String status) {
        String sql = "INSERT INTO maintenance_requests(student_id, description, status) " +
                "SELECT ?, ?, ? WHERE NOT EXISTS (" +
                "SELECT 1 FROM maintenance_requests WHERE student_id = ? AND description = ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setString(2, description);
            statement.setString(3, status);
            statement.setInt(4, studentId);
            statement.setString(5, description);
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
