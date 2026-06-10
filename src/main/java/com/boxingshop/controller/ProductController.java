package com.boxingshop.controller;

import com.boxingshop.model.Product;
import com.boxingshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products
     * Parámetros opcionales:
     *   - categoria: filtrar por categoría
     *   - search: buscar por nombre
     *   - minPrecio / maxPrecio: rango de precios
     *   - soloStock: true -> solo productos con stock
     *   - sortBy: nombre | precio | stock
     *   - sortDir: asc | desc
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAll(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double minPrecio,
            @RequestParam(required = false) Double maxPrecio,
            @RequestParam(required = false) Boolean soloStock,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        List<Product> productos = productService.getAll(
                categoria, search, minPrecio, maxPrecio, soloStock, sortBy, sortDir);
        return ResponseEntity.ok(productos);
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product created = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        try {
            Product updated = productService.update(id, product);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
