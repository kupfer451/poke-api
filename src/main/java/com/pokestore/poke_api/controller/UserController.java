package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.CreateUserDTO;
import com.pokestore.poke_api.dto.UserDTO;
import com.pokestore.poke_api.service.SupabaseService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final SupabaseService supabaseService;

    public UserController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    /**
     * GET /api/users - Obtiene todos los usuarios
     */
    @GetMapping
    public Mono<ResponseEntity<List<UserDTO>>> getAllUsers() {
        return supabaseService.selectAll("users", new ParameterizedTypeReference<List<UserDTO>>() {})
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<List<UserDTO>>notFound().build()));
    }

    /**
     * GET /api/users/{id} - Obtiene un usuario por ID
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> getUserById(@PathVariable String id) {
        return supabaseService.selectById("users", id, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * GET /api/users/email/{email} - Obtiene un usuario por email
     */
    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<UserDTO>> getUserByEmail(@PathVariable String email) {
        String filter = "email=eq." + email;
        return supabaseService.selectWithFilter("users", filter, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * POST /api/users - Crea un nuevo usuario
     */
    @PostMapping
    public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody CreateUserDTO createUserDTO) {
        // Crear el objeto con hash_pass (aquí deberías hashear la contraseña)
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", createUserDTO.getUsername());
        userData.put("hash_pass", createUserDTO.getPassword()); // TODO: Hashear la contraseña
        userData.put("email", createUserDTO.getEmail());
        userData.put("rut", createUserDTO.getRut());
        
        return supabaseService.insert("users", userData, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>badRequest().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * PATCH /api/users/{id} - Actualiza un usuario existente
     */
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> updateUser(
            @PathVariable String id, 
            @RequestBody Map<String, Object> updates) {
        return supabaseService.update("users", id, updates, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * DELETE /api/users/{id} - Elimina un usuario
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        return supabaseService.delete("users", id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.<Void>notFound().build()));
    }
}
