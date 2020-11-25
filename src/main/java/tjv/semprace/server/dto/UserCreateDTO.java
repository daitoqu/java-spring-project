package tjv.semprace.server.dto;
import java.util.List;

public class UserCreateDTO {
    private final String firstName;
    private final String lastName;
    private final List<Integer> friendsIds;

    public UserCreateDTO(String firstName, String lastName, List<Integer> friendsIds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendsIds = friendsIds;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Integer> getFriendsIds() {
        return friendsIds;
    }
}
