package tjv.semprace.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tjv.semprace.server.dto.CommentCreateDTO;
import tjv.semprace.server.dto.CommentDTO;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class ClientCommentService {

    private final WebClient webClient;

    @Autowired
    private Gson gson;

    private String baseURI = "localhost:8081/comments";

    public ClientCommentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseURI).build();
    }

    public void edit(Integer id, CommentCreateDTO commentCreateDTO) throws Exception {
        String commentJSON = gson.toJson(commentCreateDTO, CommentCreateDTO.class);
        webClient.put()
                .uri(baseURI + "/" + id.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentCreateDTO))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void create(CommentCreateDTO commentCreateDTO) throws Exception {
        String commentJSON = gson.toJson(commentCreateDTO, CommentCreateDTO.class);
        webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(commentJSON))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public CommentDTO get(Integer id) throws Exception {
        String result = webClient.get()
                .uri(baseURI + "/" + id.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return gson.fromJson(result, CommentDTO.class);
    }

    public void del(Integer id) throws Exception {
        webClient.delete()
                .uri(baseURI + "/" + id.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Collection<CommentDTO> getAll() throws Exception {
        Mono<String> result = webClient.get()
                .retrieve()
                .bodyToMono(String.class);
        Optional<String> commentDTOJson = Optional.empty();
        commentDTOJson = result.blockOptional(Duration.ofSeconds(1));

        if (commentDTOJson.isPresent()) {
            Type collectionType = new TypeToken<Collection<CommentDTO>>(){}.getType();
            return gson.fromJson(commentDTOJson.get(), collectionType);
        }
        return Collections.emptyList();
    }

}