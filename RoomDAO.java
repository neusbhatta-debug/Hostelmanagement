package hostelmanagement.db;

import hostelmanagement.model.Room;
import hostelmanagement.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("room_no"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return rooms;
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'AVAILABLE' ORDER BY room_no";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rooms.add(new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("room_no"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return rooms;
    }

    public Room getRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Room(resultSet.getInt("id"), resultSet.getString("room_no"), resultSet.getString("status"));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms(room_no, status) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, room.getRoomNo());
            statement.setString(2, room.getStatus());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean allocateRoomToStudent(int studentId, int roomId) {
        Room room = getRoomById(roomId);
        if (room == null || !"AVAILABLE".equalsIgnoreCase(room.getStatus())) {
            return false;
        }

        StudentDAO studentDAO = new StudentDAO();
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            return false;
        }

        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            if (student.getRoomId() != null) {
                try (PreparedStatement releaseRoom = connection.prepareStatement("UPDATE rooms SET status = 'AVAILABLE' WHERE id = ?")) {
                    releaseRoom.setInt(1, student.getRoomId());
                    releaseRoom.executeUpdate();
                }
            }
            try (PreparedStatement updateStudent = connection.prepareStatement("UPDATE students SET room_id = ? WHERE id = ?");
                 PreparedStatement updateRoom = connection.prepareStatement("UPDATE rooms SET status = 'OCCUPIED' WHERE id = ?")) {
                updateStudent.setInt(1, roomId);
                updateStudent.setInt(2, studentId);
                updateStudent.executeUpdate();

                updateRoom.setInt(1, roomId);
                updateRoom.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException inner) {
                connection.rollback();
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean releaseRoom(int roomId) {
        String sql = "UPDATE rooms SET status = 'AVAILABLE' WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }

    public String getRoomNumberById(Integer roomId) {
        if (roomId == null) {
            return "Not Assigned";
        }
        Room room = getRoomById(roomId);
        return room == null ? "Not Assigned" : room.getRoomNo();
    }
}
