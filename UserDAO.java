package hostelmanagement.db;

import hostelmanagement.model.Admin;
import hostelmanagement.model.Student;
import hostelmanagement.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User login(String username, String password, String role) {
        String sql = "SELECT username, password, role FROM users WHERE username = ? AND password = ? AND role = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if ("ADMIN".equalsIgnoreCase(role)) {
                        return new Admin(resultSet.getString("username"), resultSet.getString("password"));
                    }
                    return new StudentDAO().getStudentByUsername(username);
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public boolean createStudentUser(String username, String password) {
        String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, 'STUDENT')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean updateStudentUser(String oldUsername, String username, String password) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE username = ? AND role = 'STUDENT'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, oldUsername);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean deleteStudentUser(String username) {
        String sql = "DELETE FROM users WHERE username = ? AND role = 'STUDENT'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }
}
