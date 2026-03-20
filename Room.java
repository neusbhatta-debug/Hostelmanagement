package hostelmanagement.model;

public class Room {
    private int id;
    private String roomNo;
    private String status;

    public Room() {
    }

    public Room(int id, String roomNo, String status) {
        this.id = id;
        this.roomNo = roomNo;
        this.status = status;
    }

    public int getId() {
        return id;
    }    

    public void setId(int id) {
        this.id = id;
    }    

    public String getRoomNo() {
        return roomNo;
    }    

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }    

    public String getStatus() {
        return status;
    }    

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return roomNo;
    }
}
