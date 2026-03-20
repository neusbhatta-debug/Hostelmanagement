package hostelmanagement.controller;

import hostelmanagement.db.MaintenanceRequestDAO;
import hostelmanagement.model.MaintenanceRequest;
import hostelmanagement.util.AlertHelper;
import hostelmanagement.util.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MaintenanceController {
    private final MaintenanceRequestDAO maintenanceRequestDAO;

    public MaintenanceController() {
        this.maintenanceRequestDAO = new MaintenanceRequestDAO();
    }

    public ObservableList<MaintenanceRequest> getAllRequests() {
        return FXCollections.observableArrayList(maintenanceRequestDAO.getAllRequests());
    }

    public ObservableList<MaintenanceRequest> getRequestsByStudentId(int studentId) {
        return FXCollections.observableArrayList(maintenanceRequestDAO.getRequestsByStudentId(studentId));
    }

    public void submitRequest(int studentId, String description) {
        if (ValidationUtil.isBlank(description)) {
            AlertHelper.showError("Validation Error", "Description is required.");
            return;
        }
        boolean saved = maintenanceRequestDAO.addRequest(new MaintenanceRequest(0, studentId, description, "PENDING"));
        if (saved) {
            AlertHelper.showInformation("Success", "Complaint submitted successfully.");
        } else {
            AlertHelper.showError("Error", "Could not submit complaint.");
        }
    }

    public void updateStatus(MaintenanceRequest request, String newStatus) {
        if (request == null) {
            AlertHelper.showError("Selection Error", "Please select a request first.");
            return;
        }
        boolean updated = maintenanceRequestDAO.updateStatus(request.getRequestId(), newStatus);
        if (updated) {
            AlertHelper.showInformation("Success", "Complaint status updated.");
        } else {
            AlertHelper.showError("Error", "Could not update status.");
        }
    }
}
