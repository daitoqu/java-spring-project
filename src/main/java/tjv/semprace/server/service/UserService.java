package tjv.semprace.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tjv.semprace.server.dto.UserCreateDTO;
import tjv.semprace.server.dto.UserDTO;
import tjv.semprace.server.entity.User;
import tjv.semprace.server.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<User> findAllByIds(List<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<UserDTO> findByIdAsDTO(Integer id) {
        return toDTO(findById(id));
    }

    @Transactional
    public UserDTO create(UserCreateDTO userCreateDTO) throws Exception {
        List<User> friends = userRepository.findAllById(userCreateDTO.getFriendsIds());
        if (friends.size() != userCreateDTO.getFriendsIds().size())
            throw new Exception("Not all friends were found");

        User user = new User(
                userCreateDTO.getFirstName(),
                userCreateDTO.getLastName(),
                friends
        );

        return toDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(Integer id, UserCreateDTO userCreateDTO) throws Exception {
        Optional<User> oldUser = userRepository.findById(id);
        if (oldUser.isEmpty())
            throw new Exception("No such user found");
        User user = oldUser.get();
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());

        List<User> friends = userRepository.findAllById(userCreateDTO.getFriendsIds());
        if(friends.size() != userCreateDTO.getFriendsIds().size())
            throw new Exception("Some friends not found");
        user.setFriends(friends);

        return toDTO(user);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFriends()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()));
    }

    private Optional<UserDTO> toDTO(Optional<User> user) {
        if (user.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(user.get()));
    }
}
