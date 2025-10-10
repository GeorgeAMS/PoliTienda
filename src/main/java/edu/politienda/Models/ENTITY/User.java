package edu.politienda.Models.ENTITY;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// ---- Definir la clase como una entidad y mapearla a una tabla ----
@Entity
@Table (name = "Usuario")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdUsuario;
    
    @NotBlank(message = "Recuerda, el mensaje no puede estar vacio.")
    @Size(min = 3, max = 50, message = "Oelo compita, el nombre debe tener entre 3 o 50 caracteres. " )
    private String Nombre;

    @NotBlank(message = "Recuerda que el campo de tu correo del poli es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@elpoli\\.edu\\.co$", message = "El correo debe pertenecer al dominio institucional @elpoli.edu.co")
    @Column(unique = true)
    private String emailInstitucional;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

     @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,10}", message = "El teléfono debe tener entre 7 y 10 dígitos numéricos")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    private Rol rol;

    //Obligatorio para que JPA cree objetos automaticamente desde la BD.
    public User() { 

    }

    public User(String nombre, String emailInstitucional, String contraseña, String telefono, Rol rol) {
    
    this.Nombre = nombre;
    this.emailInstitucional = emailInstitucional;
    this.contrasena = contraseña;
    this.telefono = telefono;
    this.rol = rol;

    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEmailInstitucional() {
        return emailInstitucional;
    }

    public void setEmailInstitucional(String emailInstitucional) {
        this.emailInstitucional = emailInstitucional;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }


}

