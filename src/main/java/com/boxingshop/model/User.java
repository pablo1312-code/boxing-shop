package com.boxingshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Email(message = "Email no válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.USER;

    // Campos extra relacionados con boxeo
    private String categoria;      // amateur, semipro, profesional
    private String peso;           // peso del boxeador (ej: "welter", "pesado")
    private String gimnasio;       // gimnasio al que pertenece
    private Integer experiencia;   // años de experiencia

    public enum Rol {
        ADMIN, USER
    }

    // Constructores
    public User() {}

    public User(String nombre, String email, String password, Rol rol,
                String categoria, String peso, String gimnasio, Integer experiencia) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.categoria = categoria;
        this.peso = peso;
        this.gimnasio = gimnasio;
        this.experiencia = experiencia;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public String getGimnasio() { return gimnasio; }
    public void setGimnasio(String gimnasio) { this.gimnasio = gimnasio; }

    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }
}
