package com.example.LiterAlura.client;

import com.example.LiterAlura.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class GutendexClient {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(GutendexClient.class);

    public GutendexClient(@Value("${gutendex.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .filter(logRequest())
                .build();
    }

    public Mono<Book> searchBookByTitle(String title) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books")
                        .queryParam("title", title)
                        .build())
                .retrieve()
                .onStatus(status -> status.is3xxRedirection(), clientResponse ->
                        clientResponse.headers().asHttpHeaders().getLocation() != null ?
                                Mono.error(new WebClientResponseException(
                                        "Redirection detected", clientResponse.statusCode().value(), clientResponse.statusCode().toString(),
                                        clientResponse.headers().asHttpHeaders(), null, null)) :
                                Mono.error(new WebClientResponseException(
                                        "Redirection detected", clientResponse.statusCode().value(), clientResponse.statusCode().toString(),
                                        null, null, null)))
                .bodyToMono(Book.class)
                .doOnError(WebClientResponseException.class, e -> {
                    if (e.getStatusCode().is3xxRedirection()) {
                        String redirectUrl = e.getHeaders().getLocation().toString();
                        logger.info("Redireccionado a: {}", redirectUrl);
                        this.webClient.get()
                                .uri(redirectUrl)
                                .retrieve()
                                .bodyToMono(Book.class)
                                .doOnError(redirError -> logger.error("Error al buscar libro por título en URL redireccionada: {}", redirError.getMessage()))
                                .subscribe();
                    } else {
                        logger.error("Error al buscar libro por título: {}", e.getMessage());
                    }
                });
    }


    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
