package com.pokestore.poke_api.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class SupabaseService {

    private final WebClient webClient;

    public SupabaseService(WebClient supabaseWebClient) {
        this.webClient = supabaseWebClient;
    }

    /**
     * SELECT: Obtiene todos los registros de una tabla
     */
    public <T> Mono<List<T>> selectAll(String table, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.get()
                .uri("/rest/v1/" + table)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en selectAll: " + e.getResponseBodyAsString());
                    return Mono.just(Collections.emptyList());
                });
    }

    /**
     * SELECT: Obtiene un registro por ID
     */
    public <T> Mono<List<T>> selectById(String table, String id, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.get()
                .uri("/rest/v1/" + table + "?id=eq." + id)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en selectById: " + e.getResponseBodyAsString());
                    return Mono.just(Collections.emptyList());
                });
    }

    /**
     * SELECT: Búsqueda con filtros personalizados
     */
    public <T> Mono<List<T>> selectWithFilter(String table, String filter, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.get()
                .uri("/rest/v1/" + table + "?" + filter)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en selectWithFilter: " + e.getResponseBodyAsString());
                    return Mono.just(Collections.emptyList());
                });
    }

    /**
     * INSERT: Inserta un nuevo registro
     */
    public <T> Mono<List<T>> insert(String table, Object body, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.post()
                .uri("/rest/v1/" + table)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .doOnNext(result -> System.out.println("Insert exitoso en " + table + ": " + result))
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en insert (" + table + "): " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
                    return Mono.just(Collections.emptyList());
                });
    }

    /**
     * UPDATE: Actualiza un registro por ID
     */
    public <T> Mono<List<T>> update(String table, String id, Object body, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.patch()
                .uri("/rest/v1/" + table + "?id=eq." + id)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en update: " + e.getResponseBodyAsString());
                    return Mono.just(Collections.emptyList());
                });
    }

    /**
     * DELETE: Elimina un registro por ID
     */
    public Mono<Void> delete(String table, String id) {
        return webClient.delete()
                .uri("/rest/v1/" + table + "?id=eq." + id)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en delete: " + e.getResponseBodyAsString());
                    return Mono.empty();
                });
    }

    /**
     * DELETE: Elimina registros con filtro personalizado
     */
    public Mono<Void> deleteWithFilter(String table, String filter) {
        return webClient.delete()
                .uri("/rest/v1/" + table + "?" + filter)
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    System.err.println("Error en deleteWithFilter: " + e.getResponseBodyAsString());
                    return Mono.empty();
                });
    }
}
