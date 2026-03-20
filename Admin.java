package hostelmanagement.model;

public class Admin extends User {

    public Admin() {
    }

    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
