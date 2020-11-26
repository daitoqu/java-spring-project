package tjv.semprace.server.dto;
import java.util.List;

public class UserDTO {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final List<Integer> friendsIds;

    public UserDTO(int id, String firstName, String lastName, List<Integer> friendsIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendsIds = friendsIds;
    }

    public UserDTO(int id, UserCreateDTO userCreateDTO) {
        this.id = id;
        this.firstName = userCreateDTO.getFirstName();
        this.lastName = userCreateDTO.getLastName();
        this.friendsIds = userCreateDTO.getFriendsIds();
    }

    public int getId() {
        return id;
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
