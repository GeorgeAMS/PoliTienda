package edu.politienda.Models.DTO;

public class loginDTO {
    private String emailInstitucional;
    private String contrasena;
    
    // Constructor vacío
    public loginDTO() {}

    // Getters y Setters
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
}