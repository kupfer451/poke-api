package com.pokestore.poke_api.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
                .bodyToMono(responseType);
    }

    /**
     * SELECT: Obtiene un registro por ID
     */
    public <T> Mono<List<T>> selectById(String table, String id, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.get()
                .uri("/rest/v1/" + table + "?id=eq." + id)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * SELECT: Búsqueda con filtros personalizados
     */
    public <T> Mono<List<T>> selectWithFilter(String table, String filter, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.get()
                .uri("/rest/v1/" + table + "?" + filter)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * INSERT: Inserta un nuevo registro
     */
    public <T> Mono<List<T>> insert(String table, Object body, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.post()
                .uri("/rest/v1/" + table)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * UPDATE: Actualiza un registro por ID
     */
    public <T> Mono<List<T>> update(String table, String id, Object body, ParameterizedTypeReference<List<T>> responseType) {
        return webClient.patch()
                .uri("/rest/v1/" + table + "?id=eq." + id)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType);
    }

    /**
     * DELETE: Elimina un registro por ID
     */
    public Mono<Void> delete(String table, String id) {
        return webClient.delete()
                .uri("/rest/v1/" + table + "?id=eq." + id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
