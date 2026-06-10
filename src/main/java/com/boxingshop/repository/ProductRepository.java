package com.boxingshop.repository;

import com.boxingshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Filtrar por categoría
    List<Product> findByCategoria(String categoria);

    // Buscar por nombre (texto parcial, sin distinguir mayúsculas)
    List<Product> findByNombreContainingIgnoreCase(String nombre);

    // Filtrar por stock disponible (stock > 0)
    List<Product> findByStockGreaterThan(Integer stock);

    // Filtrar por precio menor o igual
    List<Product> findByPrecioLessThanEqual(Double maxPrecio);

    // Filtrar por precio mayor o igual
    List<Product> findByPrecioGreaterThanEqual(Double minPrecio);

    // Filtrar por rango de precio
    List<Product> findByPrecioBetween(Double minPrecio, Double maxPrecio);

    // Filtro combinado: categoría + búsqueda por nombre
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoria IS NULL OR p.categoria = :categoria) AND " +
           "(:search IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:minPrecio IS NULL OR p.precio >= :minPrecio) AND " +
           "(:maxPrecio IS NULL OR p.precio <= :maxPrecio) AND " +
           "(:soloStock = false OR p.stock > 0)")
    List<Product> filtrar(
            @Param("categoria") String categoria,
            @Param("search") String search,
            @Param("minPrecio") Double minPrecio,
            @Param("maxPrecio") Double maxPrecio,
            @Param("soloStock") boolean soloStock
    );
}
