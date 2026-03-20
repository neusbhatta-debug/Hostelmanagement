package hostelmanagement.model;

public class MaintenanceRequest {
    private int requestId;
    private int studentId;
    private String description;
    private String status;

    public MaintenanceRequest() {
    }

    public MaintenanceRequest(int requestId, int studentId, String description, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.description = description;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }    

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }    

    public int getStudentId() {
        return studentId;
    }    

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }    

    public String getDescription() {
        return description;
    }    

    public void setDescription(String description) {
        this.description = description;
    }    

    public String getStatus() {
        return status;
    }    

    public void setStatus(String status) {
        this.status = status;
    }
}
