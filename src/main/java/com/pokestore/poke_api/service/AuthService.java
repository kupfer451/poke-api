package com.pokestore.poke_api.service;

import com.pokestore.poke_api.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final SupabaseService supabaseService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Registra un nuevo usuario
     */
    public Mono<AuthResponseDTO> register(CreateUserDTO createUserDTO) {
        // Verificar si el email ya existe
        return checkEmailExists(createUserDTO.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.just(AuthResponseDTO.error("El email ya está registrado"));
                    }
                    
                    // Verificar si el RUT ya existe
                    return checkRutExists(createUserDTO.getRut())
                            .flatMap(rutExists -> {
                                if (rutExists) {
                                    return Mono.just(AuthResponseDTO.error("El RUT ya está registrado"));
                                }
                                
                                // Crear usuario con password hasheado
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("username", createUserDTO.getUsername());
                                userData.put("hash_pass", passwordEncoder.encode(createUserDTO.getPassword()));
                                userData.put("email", createUserDTO.getEmail());
                                userData.put("rut", createUserDTO.getRut());
                                userData.put("isAdmin", createUserDTO.getIsAdmin() != null ? createUserDTO.getIsAdmin() : false);
                                
                                return supabaseService.insert("users", userData, 
                                        new ParameterizedTypeReference<List<UserDTO>>() {})
                                        .flatMap(list -> {
                                            if (list == null || list.isEmpty()) {
                                                return Mono.just(AuthResponseDTO.error("Error al crear usuario"));
                                            }
                                            return Mono.just(AuthResponseDTO.success("Usuario registrado exitosamente", list.get(0)));
                                        });
                            });
                });
    }

    /**
     * Inicia sesión con email y password
     */
    public Mono<AuthResponseDTO> login(LoginDTO loginDTO) {
        String filter = "email=eq." + loginDTO.getEmail();
        
        return supabaseService.selectWithFilter("users", filter, 
                new ParameterizedTypeReference<List<UserWithPasswordDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(AuthResponseDTO.error("Usuario no encontrado"));
                    }
                    
                    UserWithPasswordDTO user = list.get(0);
                    
                    // Verificar password
                    if (!passwordEncoder.matches(loginDTO.getPassword(), user.getHashPass())) {
                        return Mono.just(AuthResponseDTO.error("Contraseña incorrecta"));
                    }
                    
                    return Mono.just(AuthResponseDTO.success("Login exitoso", user.toUserDTO()));
                });
    }

    /**
     * Verifica si un usuario existe por email y devuelve su información
     */
    public Mono<AuthResponseDTO> verifyUser(String email) {
        String filter = "email=eq." + email;
        
        return supabaseService.selectWithFilter("users", filter, 
                new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(AuthResponseDTO.error("Usuario no encontrado"));
                    }
                    return Mono.just(AuthResponseDTO.success("Usuario encontrado", list.get(0)));
                });
    }

    /**
     * Verifica si un email ya existe
     */
    public Mono<Boolean> checkEmailExists(String email) {
        String filter = "email=eq." + email;
        return supabaseService.selectWithFilter("users", filter, 
                new ParameterizedTypeReference<List<UserDTO>>() {})
                .map(list -> list != null && !list.isEmpty())
                .defaultIfEmpty(false);
    }

    /**
     * Verifica si un RUT ya existe
     */
    public Mono<Boolean> checkRutExists(String rut) {
        String filter = "rut=eq." + rut;
        return supabaseService.selectWithFilter("users", filter, 
                new ParameterizedTypeReference<List<UserDTO>>() {})
                .map(list -> list != null && !list.isEmpty())
                .defaultIfEmpty(false);
    }
}
