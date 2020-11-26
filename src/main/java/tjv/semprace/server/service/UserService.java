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
    public void addFriend(Integer user1id, Integer user2id) throws Exception {
        if (user1id == user2id)
            throw new Exception("Can't add yourself to friends");
        Optional<User> user1opt = findById(user1id);
        Optional<User> user2opt = findById(user2id);
        if (user1opt.isEmpty() || user2opt.isEmpty())
            throw new Exception("No such user");

        User user1 = user1opt.get();
        User user2 = user2opt.get();
        if (user1.getFriends().contains(user2) || user2.getFriends().contains(user1))
            throw new Exception("Already friends");
        List<User> friends = user1.getFriends();
        friends.add(user2);
        user1.setFriends(friends);
        friends = user2.getFriends();
        friends.add(user1);
        user2.setFriends(friends);
    }

    @Transactional
    public void deleteFriend(Integer user1id, Integer user2id) throws Exception {
        if (user1id == user2id)
            throw new Exception("Can't delete yourself from friends");
        Optional<User> user1opt = findById(user1id);
        Optional<User> user2opt = findById(user2id);
        if (user1opt.isEmpty() || user2opt.isEmpty())
            throw new Exception("No such user");

        User user1 = user1opt.get();
        User user2 = user2opt.get();
        if (!user1.getFriends().contains(user2) || !user2.getFriends().contains(user1))
            throw new Exception("Already not friends");
        List<User> friends = user1.getFriends();
        friends.remove(user2);
        user1.setFriends(friends);
        friends = user2.getFriends();
        friends.remove(user1);
        user2.setFriends(friends);
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

    @Transactional
    public void delete(Integer id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new Exception("No such user found");

        userRepository.delete(user.get());
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
