package com.example.LiterAlura;

import com.example.LiterAlura.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class BookClient {

    private final WebClient webClient;

    @Autowired
    public BookClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://gutendex.com").build();
    }

    public Mono<Book> getBookByTitle(String title) {
        return this.webClient.get()
                .uri("/books?search={title}", title)
                .retrieve()
                .bodyToMono(Book.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    System.err.println("Error: " + ex.getMessage());
                    return Mono.empty();
                });
    }
}
