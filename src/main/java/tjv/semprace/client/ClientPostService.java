package tjv.semprace.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tjv.semprace.server.dto.PostCreateDTO;
import tjv.semprace.server.dto.PostDTO;

import java.lang.reflect.Type;
import java.util.Collection;

@Service
public class ClientPostService {

    private final WebClient webClient;

    @Autowired
    private Gson gson;

    private String baseURI = "localhost:8081/posts";

    public ClientPostService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseURI).build();
    }

    public void edit(Integer id, PostCreateDTO postCreateDTO) throws Exception {
        String postJSON = gson.toJson(postCreateDTO, PostCreateDTO.class);
        webClient.put()
                .uri(baseURI + "/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(postCreateDTO))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void create(PostCreateDTO postCreateDTO) throws Exception {
        String userJSON = gson.toJson(postCreateDTO, PostCreateDTO.class);
        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(userJSON))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public PostDTO get(Integer id) throws Exception {
        String result = webClient.get()
                .uri(baseURI + "/" + id.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return gson.fromJson(result, PostDTO.class);
    }

    public void del(Integer id) throws Exception {
        webClient.delete()
                .uri(baseURI + "/" + id.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Collection<PostDTO> getAll() throws Exception {
        String result = webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Type collectionType = new TypeToken<Collection<PostDTO>>(){}.getType();
        return gson.fromJson(result, collectionType);
    }

}