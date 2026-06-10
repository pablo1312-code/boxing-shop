package com.boxingshop.service;

import com.boxingshop.model.User;
import com.boxingshop.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User usuario;

    @BeforeEach
    void setUp() {
        usuario = new User("Carlos López", "carlos@boxing.com", "1234",
                User.Rol.USER, "amateur", "welter", "Gym Madrid", 3);
        usuario.setId(1L);
    }

    @Test
    void testCrearUsuario() {
        when(userRepository.existsByEmail("carlos@boxing.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(usuario);

        User resultado = userService.create(usuario);

        assertNotNull(resultado);
        assertEquals("Carlos López", resultado.getNombre());
        assertEquals(User.Rol.USER, resultado.getRol());
    }

    @Test
    void testCrearUsuarioEmailDuplicado() {
        when(userRepository.existsByEmail("carlos@boxing.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(usuario));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testObtenerUsuarios() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(usuario));

        List<User> resultado = userService.getAll();

        assertEquals(1, resultado.size());
        assertEquals("Carlos López", resultado.get(0).getNombre());
    }

    @Test
    void testObtenerUsuarioPorId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<User> resultado = userService.getById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Gym Madrid", resultado.get().getGimnasio());
    }

    @Test
    void testEliminarUsuario() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testActualizarUsuario() {
        User actualizado = new User("Carlos Updated", "carlos@boxing.com", "nueva",
                User.Rol.ADMIN, "profesional", "pesado", "Gym BCN", 10);
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(userRepository.save(any(User.class))).thenReturn(actualizado);

        User resultado = userService.update(1L, actualizado);

        assertEquals("Carlos Updated", resultado.getNombre());
        assertEquals(User.Rol.ADMIN, resultado.getRol());
    }
}
