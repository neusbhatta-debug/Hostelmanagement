package hostelmanagement.util;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidContact(String contact) {
        return contact != null && contact.matches("\\d{7,15}");
    }
}
