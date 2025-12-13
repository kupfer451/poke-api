package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.ProductDTO;
import com.pokestore.poke_api.service.SupabaseService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final SupabaseService supabaseService;

    public ProductController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    /**
     * GET /api/products - Obtiene todos los productos
     */
    @GetMapping
    public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
        return supabaseService.selectAll("products", new ParameterizedTypeReference<List<ProductDTO>>() {})
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<List<ProductDTO>>notFound().build()));
    }

    /**
     * GET /api/products/{id} - Obtiene un producto por ID
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable String id) {
        return supabaseService.selectById("products", id, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<ProductDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * GET /api/products/search?name=xxx - Busca productos por nombre
     */
    @GetMapping("/search")
    public Mono<ResponseEntity<List<ProductDTO>>> searchProductsByName(@RequestParam String name) {
        String filter = "product_name=ilike.*" + name + "*";
        return supabaseService.selectWithFilter("products", filter, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<List<ProductDTO>>notFound().build()));
    }

    /**
     * POST /api/products - Crea un nuevo producto
     */
    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) {
        return supabaseService.insert("products", productDTO, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<ProductDTO>badRequest().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * PATCH /api/products/{id} - Actualiza un producto existente
     */
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> updateProduct(
            @PathVariable String id, 
            @RequestBody ProductDTO productDTO) {
        return supabaseService.update("products", id, productDTO, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<ProductDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    /**
     * DELETE /api/products/{id} - Elimina un producto
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id) {
        return supabaseService.delete("products", id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.<Void>notFound().build()));
    }
}
