package tjv.semprace.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tjv.semprace.server.dto.UserCreateNewDTO;
import tjv.semprace.server.dto.UserDTO;

import java.lang.reflect.Type;
import java.util.Collection;

@Service
public class ClientUserService {

    private final WebClient webClient;

    @Autowired
    private Gson gson;

    private String baseURI = "localhost:8081/users";

    public ClientUserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseURI).build();
    }

    public void edit(Integer id, UserCreateNewDTO newUserDTO) throws Exception {
        String userJSON = gson.toJson(newUserDTO, UserCreateNewDTO.class);
        webClient.put()
                .uri(baseURI + "/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userJSON))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void create(UserCreateNewDTO newUserDTO) throws Exception {
        String userJSON = gson.toJson(newUserDTO, UserCreateNewDTO.class);
        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userJSON))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public UserDTO get(Integer id) throws Exception {
        String result = webClient.get()
                .uri(baseURI + "/" + id.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return gson.fromJson(result, UserDTO.class);
    }

    public void del(Integer id) throws Exception {
        webClient.delete()
                .uri(baseURI + "/" + id.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void addFriend(Integer first, Integer second) throws Exception {
        webClient.put()
                .uri(baseURI + "/" + first.toString() + "/" + second.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void delFriend(Integer first, Integer second) throws Exception {
        webClient.delete()
                .uri(baseURI + "/" + first.toString() + "/" + second.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Collection<UserDTO> getAll() throws Exception {
        String result = webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Type collectionType = new TypeToken<Collection<UserDTO>>(){}.getType();
        return gson.fromJson(result, collectionType);
    }

}