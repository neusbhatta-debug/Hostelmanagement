package hostelmanagement.controller;

import hostelmanagement.db.FeeDAO;
import hostelmanagement.model.Fee;
import hostelmanagement.util.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FeeController {
    private final FeeDAO feeDAO;

    public FeeController() {
        this.feeDAO = new FeeDAO();
    }

    public ObservableList<Fee> getAllFees() {
        return FXCollections.observableArrayList(feeDAO.getAllFees());
    }

    public ObservableList<Fee> getFeesByStudentId(int studentId) {
        return FXCollections.observableArrayList(feeDAO.getFeesByStudentId(studentId));
    }

    public void recordPayment(int studentId, double amount, String status) {
        if (amount <= 0) {
            AlertHelper.showError("Validation Error", "Amount must be greater than 0.");
            return;
        }
        boolean saved = feeDAO.recordPayment(new Fee(0, studentId, amount, status));
        if (saved) {
            AlertHelper.showInformation("Success", "Fee record saved successfully.");
        } else {
            AlertHelper.showError("Error", "Could not save fee record.");
        }
    }
}
