package com.boxingshop.service;

import com.boxingshop.model.Product;
import com.boxingshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product producto;

    @BeforeEach
    void setUp() {
        producto = new Product("Guantes de boxeo", "Guantes profesionales 16oz", 59.99, 10, "guantes");
        producto.setId(1L);
    }

    @Test
    void testCrearProducto() {
        when(productRepository.save(any(Product.class))).thenReturn(producto);

        Product resultado = productService.create(producto);

        assertNotNull(resultado);
        assertEquals("Guantes de boxeo", resultado.getNombre());
        assertEquals(59.99, resultado.getPrecio());
        verify(productRepository, times(1)).save(producto);
    }

    @Test
    void testObtenerProductoPorId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Product> resultado = productService.getById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("guantes", resultado.get().getCategoria());
    }

    @Test
    void testObtenerProductoNoExistente() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> resultado = productService.getById(99L);

        assertFalse(resultado.isPresent());
    }

    @Test
    void testEliminarProducto() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.delete(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProductoNoExistente() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productService.delete(99L));
    }

    @Test
    void testActualizarProducto() {
        Product actualizado = new Product("Guantes pro", "Actualizados", 79.99, 5, "guantes");
        when(productRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productRepository.save(any(Product.class))).thenReturn(actualizado);

        Product resultado = productService.update(1L, actualizado);

        assertEquals("Guantes pro", resultado.getNombre());
        assertEquals(79.99, resultado.getPrecio());
    }

    @Test
    void testGetAllSinFiltros() {
        List<Product> lista = Arrays.asList(producto);
        when(productRepository.filtrar(null, null, null, null, false)).thenReturn(lista);

        List<Product> resultado = productService.getAll(null, null, null, null, null, null, null);

        assertEquals(1, resultado.size());
    }

    @Test
    void testGetAllOrdenadoPorPrecioAsc() {
        Product p1 = new Product("Casco", "desc", 30.0, 5, "proteccion");
        Product p2 = new Product("Guantes", "desc", 60.0, 10, "guantes");
        when(productRepository.filtrar(null, null, null, null, false))
                .thenReturn(Arrays.asList(p2, p1));

        List<Product> resultado = productService.getAll(null, null, null, null, null, "precio", "asc");

        assertEquals(30.0, resultado.get(0).getPrecio());
        assertEquals(60.0, resultado.get(1).getPrecio());
    }
}
