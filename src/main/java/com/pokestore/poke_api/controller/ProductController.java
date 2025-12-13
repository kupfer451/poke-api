package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.ProductDTO;
import com.pokestore.poke_api.service.SupabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Operaciones CRUD para gestión de productos del catálogo")
public class ProductController {

    private final SupabaseService supabaseService;

    public ProductController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @Operation(
            summary = "Obtener todos los productos",
            description = "Retorna una lista con todos los productos disponibles en el catálogo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de productos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron productos"
            )
    })
    @GetMapping
    public Mono<ResponseEntity<List<ProductDTO>>> getAllProducts() {
        return supabaseService.selectAll("products", new ParameterizedTypeReference<List<ProductDTO>>() {})
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<List<ProductDTO>>notFound().build()));
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Retorna la información detallada de un producto específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(
            @Parameter(
                    description = "ID único del producto (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable String id) {
        return supabaseService.selectById("products", id, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<ProductDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Buscar productos por nombre",
            description = "Busca productos que contengan el texto especificado en su nombre. " +
                    "La búsqueda no distingue entre mayúsculas y minúsculas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Productos encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron productos con ese nombre"
            )
    })
    @GetMapping("/search")
    public Mono<ResponseEntity<List<ProductDTO>>> searchProductsByName(
            @Parameter(
                    description = "Texto a buscar en el nombre del producto",
                    required = true,
                    example = "pikachu"
            )
            @RequestParam String name) {
        String filter = "product_name=ilike.*" + name + "*";
        return supabaseService.selectWithFilter("products", filter, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<List<ProductDTO>>notFound().build()));
    }

    @Operation(
            summary = "Crear nuevo producto",
            description = "Agrega un nuevo producto al catálogo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error al crear producto - datos inválidos"
            )
    })
    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo producto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            )
            @RequestBody ProductDTO productDTO) {
        return supabaseService.insert("products", productDTO, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<ProductDTO>badRequest().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza la información de un producto existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> updateProduct(
            @Parameter(
                    description = "ID del producto a actualizar",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos del producto a actualizar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            )
            @RequestBody ProductDTO productDTO) {
        return supabaseService.update("products", id, productDTO, new ParameterizedTypeReference<List<ProductDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<ProductDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Elimina permanentemente un producto del catálogo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(
            @Parameter(
                    description = "ID del producto a eliminar",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable String id) {
        return supabaseService.delete("products", id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.<Void>notFound().build()));
    }
}
