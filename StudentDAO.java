package hostelmanagement.db;

import hostelmanagement.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer roomId = resultSet.getObject("room_id") == null ? null : resultSet.getInt("room_id");
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("roll"),
                        resultSet.getString("contact"),
                        roomId,
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return students;
    }

    public Student getStudentByUsername(String username) {
        String sql = "SELECT * FROM students WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer roomId = resultSet.getObject("room_id") == null ? null : resultSet.getInt("room_id");
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("roll"),
                            resultSet.getString("contact"),
                            roomId,
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Integer roomId = resultSet.getObject("room_id") == null ? null : resultSet.getInt("room_id");
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("roll"),
                            resultSet.getString("contact"),
                            roomId,
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public boolean addStudent(Student student) {
        if (!new UserDAO().createStudentUser(student.getUsername(), student.getPassword())) {
            return false;
        }

        String sql = "INSERT INTO students(name, roll, contact, room_id, username, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getRoll());
            statement.setString(3, student.getContact());

            if (student.getRoomId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, student.getRoomId());
            }

            statement.setString(5, student.getUsername());
            statement.setString(6, student.getPassword());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            new UserDAO().deleteStudentUser(student.getUsername());
            return false;
        }
    }

    public boolean updateStudent(Student student, String oldUsername) {
        if (!new UserDAO().updateStudentUser(oldUsername, student.getUsername(), student.getPassword())) {
            return false;
        }

        String sql = "UPDATE students SET name = ?, roll = ?, contact = ?, room_id = ?, username = ?, password = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getRoll());
            statement.setString(3, student.getContact());
            if (student.getRoomId() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, student.getRoomId());
            }
            statement.setString(5, student.getUsername());
            statement.setString(6, student.getPassword());
            statement.setInt(7, student.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            return false;
        }

        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            boolean deleted = statement.executeUpdate() > 0;
            if (deleted) {
                new UserDAO().deleteStudentUser(student.getUsername());
            }
            return deleted;
        } catch (SQLException exception) {
            return false;
        }
    }
}
