package tjv.semprace.server.dto;
import java.util.List;

public class UserCreateNewDTO {
    private final String firstName;
    private final String lastName;

    public UserCreateNewDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
