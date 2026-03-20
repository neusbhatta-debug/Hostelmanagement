package hostelmanagement.controller;

import hostelmanagement.db.RoomDAO;
import hostelmanagement.model.Room;
import hostelmanagement.util.AlertHelper;
import hostelmanagement.util.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RoomController {
    private final RoomDAO roomDAO;

    public RoomController() {
        this.roomDAO = new RoomDAO();
    }

    public ObservableList<Room> getRooms() {
        return FXCollections.observableArrayList(roomDAO.getAllRooms());
    }

    public ObservableList<Room> getAvailableRooms() {
        return FXCollections.observableArrayList(roomDAO.getAvailableRooms());
    }

    public void addRoom(String roomNo) {
        if (ValidationUtil.isBlank(roomNo)) {
            AlertHelper.showError("Validation Error", "Room number is required.");
            return;
        }
        boolean added = roomDAO.addRoom(new Room(0, roomNo, "AVAILABLE"));
        if (added) {
            AlertHelper.showInformation("Success", "Room added successfully.");
        } else {
            AlertHelper.showError("Error", "Room number may already exist.");
        }
    }

    public void allocateRoom(int studentId, Room room) {
        if (room == null) {
            AlertHelper.showError("Selection Error", "Select an available room.");
            return;
        }
        boolean success = roomDAO.allocateRoomToStudent(studentId, room.getId());
        if (success) {
            AlertHelper.showInformation("Success", "Room allocated successfully.");
        } else {
            AlertHelper.showError("Allocation Error", "Room allocation failed.");
        }
    }
}
