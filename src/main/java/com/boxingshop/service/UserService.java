package com.boxingshop.service;

import com.boxingshop.model.User;
import com.boxingshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Obtener todos los usuarios
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Obtener usuario por ID
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    // Crear usuario
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        return userRepository.save(user);
    }

    // Actualizar usuario
    public User update(Long id, User userActualizado) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        user.setNombre(userActualizado.getNombre());
        user.setEmail(userActualizado.getEmail());
        if (userActualizado.getPassword() != null && !userActualizado.getPassword().isBlank()) {
            user.setPassword(userActualizado.getPassword());
        }
        user.setRol(userActualizado.getRol());
        user.setCategoria(userActualizado.getCategoria());
        user.setPeso(userActualizado.getPeso());
        user.setGimnasio(userActualizado.getGimnasio());
        user.setExperiencia(userActualizado.getExperiencia());

        return userRepository.save(user);
    }

    // Eliminar usuario
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }
}
