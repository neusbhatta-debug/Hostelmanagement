package hostelmanagement.model;

public class Fee {
    private int id;
    private int studentId;
    private double amount;
    private String status;

    public Fee() {
    }

    public Fee(int id, int studentId, double amount, String status) {
        this.id = id;
        this.studentId = studentId;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }    

    public void setId(int id) {
        this.id = id;
    }    

    public int getStudentId() {
        return studentId;
    }    

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }    

    public double getAmount() {
        return amount;
    }    

    public void setAmount(double amount) {
        this.amount = amount;
    }    

    public String getStatus() {
        return status;
    }    

    public void setStatus(String status) {
        this.status = status;
    }
}
