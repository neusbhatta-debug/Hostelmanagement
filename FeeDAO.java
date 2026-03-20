package hostelmanagement.db;

import hostelmanagement.model.Fee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeeDAO {

    public List<Fee> getAllFees() {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT * FROM fees ORDER BY id DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                fees.add(new Fee(
                        resultSet.getInt("id"),
                        resultSet.getInt("student_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("status")
                ));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return fees;
    }

    public List<Fee> getFeesByStudentId(int studentId) {
        List<Fee> fees = new ArrayList<>();
        String sql = "SELECT * FROM fees WHERE student_id = ? ORDER BY id DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    fees.add(new Fee(
                            resultSet.getInt("id"),
                            resultSet.getInt("student_id"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("status")
                    ));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return fees;
    }

    public boolean recordPayment(Fee fee) {
        String sql = "INSERT INTO fees(student_id, amount, status) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, fee.getStudentId());
            statement.setDouble(2, fee.getAmount());
            statement.setString(3, fee.getStatus());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            return false;
        }
    }
}
