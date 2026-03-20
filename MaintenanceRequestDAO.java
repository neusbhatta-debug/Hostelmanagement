package hostelmanagement.db;

import hostelmanagement.model.MaintenanceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRequestDAO {

    public List<MaintenanceRequest> getAllRequests() {
        List<MaintenanceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_requests ORDER BY request_id DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                requests.add(new MaintenanceRequest(
                        resultSet.getInt("request_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getString("description"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return requests;
    }

    public List<MaintenanceRequest> getRequestsByStudentId(int studentId) {
        List<MaintenanceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_requests WHERE student_id = ? ORDER BY request_id DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    requests.add(new MaintenanceRequest(
                            resultSet.getInt("request_id"),
                            resultSet.getInt("student_id"),
                            resultSet.getString("description"),
                            resultSet.getString("status")
                    ));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return requests;
    }

    public boolean addRequest(MaintenanceRequest request) {
        String sql = "INSERT INTO maintenance_requests(student_id, description, status) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, request.getStudentId());
            statement.setString(2, request.getDescription());
            statement.setString(3, request.getStatus());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }

    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE maintenance_requests SET status = ? WHERE request_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, requestId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }
}
