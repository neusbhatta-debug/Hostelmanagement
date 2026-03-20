package hostelmanagement.controller;

import hostelmanagement.db.RoomDAO;
import hostelmanagement.db.StudentDAO;
import hostelmanagement.model.Student;
import hostelmanagement.util.AlertHelper;
import hostelmanagement.util.ValidationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentController {
    private final StudentDAO studentDAO;
    private final RoomDAO roomDAO;

    public StudentController() {
        this.studentDAO = new StudentDAO();
        this.roomDAO = new RoomDAO();
    }

    public ObservableList<Student> getStudents() {
        return FXCollections.observableArrayList(studentDAO.getAllStudents());
    }

    public void addStudent(Student student) {
        String validation = validateStudent(student);
        if (validation != null) {
            AlertHelper.showError("Validation Error", validation);
            return;
        }
        boolean added = studentDAO.addStudent(student);
        if (added) {
            AlertHelper.showInformation("Success", "Student added successfully.");
        } else {
            AlertHelper.showError("Error", "Could not add student. Roll or username may already exist.");
        }
    }

    public void updateStudent(Student student, String oldUsername) {
        String validation = validateStudent(student);
        if (validation != null) {
            AlertHelper.showError("Validation Error", validation);
            return;
        }

        Student existing = studentDAO.getStudentById(student.getId());
        if (existing != null && existing.getRoomId() != null &&
                (student.getRoomId() == null || !existing.getRoomId().equals(student.getRoomId()))) {
            roomDAO.releaseRoom(existing.getRoomId());
        }

        boolean updated = studentDAO.updateStudent(student, oldUsername);
        if (updated) {
            if (student.getRoomId() != null) {
                roomDAO.allocateRoomToStudent(student.getId(), student.getRoomId());
            }
            AlertHelper.showInformation("Success", "Student updated successfully.");
        } else {
            AlertHelper.showError("Error", "Could not update student.");
        }
    }

    public void deleteStudent(Student student) {
        if (student == null) {
            AlertHelper.showError("Selection Error", "Please select a student.");
            return;
        }

        if (student.getRoomId() != null) {
            roomDAO.releaseRoom(student.getRoomId());
        }

        boolean deleted = studentDAO.deleteStudent(student.getId());
        if (deleted) {
            AlertHelper.showInformation("Success", "Student deleted successfully.");
        } else {
            AlertHelper.showError("Error", "Could not delete student.");
        }
    }

    private String validateStudent(Student student) {
        if (ValidationUtil.isBlank(student.getName()) ||
            ValidationUtil.isBlank(student.getRoll()) ||
            ValidationUtil.isBlank(student.getContact()) ||
            ValidationUtil.isBlank(student.getUsername()) ||
            ValidationUtil.isBlank(student.getPassword())) {
            return "All text fields are required.";
        }
        if (!ValidationUtil.isValidContact(student.getContact())) {
            return "Contact number should contain 7 to 15 digits.";
        }
        return null;
    }
}
