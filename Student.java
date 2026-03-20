package hostelmanagement.model;

public class Student extends User {
    private int id;
    private String name;
    private String roll;
    private String contact;
    private Integer roomId;

    public Student() {
    }

    public Student(int id, String name, String roll, String contact, Integer roomId, String username, String password) {
        super(username, password);
        this.id = id;
        this.name = name;
        this.roll = roll;
        this.contact = contact;
        this.roomId = roomId;
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }    

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }    

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
