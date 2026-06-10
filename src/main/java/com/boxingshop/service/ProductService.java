package com.boxingshop.service;

import com.boxingshop.model.Product;
import com.boxingshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Obtener todos los productos con filtros y ordenación
    public List<Product> getAll(String categoria, String search, Double minPrecio,
                                Double maxPrecio, Boolean soloStock,
                                String sortBy, String sortDir) {

        List<Product> productos = productRepository.filtrar(
                categoria,
                search,
                minPrecio,
                maxPrecio,
                soloStock != null && soloStock
        );

        // Ordenación
        if (sortBy != null) {
            Comparator<Product> comparator = switch (sortBy) {
                case "precio" -> Comparator.comparingDouble(Product::getPrecio);
                case "stock"  -> Comparator.comparingInt(Product::getStock);
                default       -> Comparator.comparing(Product::getNombre);
            };
            if ("desc".equalsIgnoreCase(sortDir)) {
                comparator = comparator.reversed();
            }
            productos.sort(comparator);
        }

        return productos;
    }

    // Obtener producto por ID
    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    // Crear producto
    public Product create(Product product) {
        return productRepository.save(product);
    }

    // Actualizar producto
    public Product update(Long id, Product productoActualizado) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        product.setNombre(productoActualizado.getNombre());
        product.setDescripcion(productoActualizado.getDescripcion());
        product.setPrecio(productoActualizado.getPrecio());
        product.setStock(productoActualizado.getStock());
        product.setCategoria(productoActualizado.getCategoria());

        return productRepository.save(product);
    }

    // Eliminar producto
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }
}
