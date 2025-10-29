package edu.politienda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.politienda.Models.DTO.loginDTO;
import edu.politienda.Services.userService;
import edu.politienda.Models.ENTITY.user;

import jakarta.servlet.http.HttpSession;

@Controller
public class loginController {

    private final userService servicioUsuario;

    public loginController(userService servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    // -------------------------------------------------------------------------
    // 1. GET: Mostrar el formulario de login
    // Mapea a /login
    // -------------------------------------------------------------------------
    @GetMapping({"/","/login"})
    public String showLoginForm(Model model) {
        // Inicializa el objeto DTO para el formulario Thymeleaf
        model.addAttribute("loginDto", new loginDTO()); 
        return "login"; // Devuelve la vista login.html
    }

    // -------------------------------------------------------------------------
    // 2. POST: Procesar la solicitud y autenticar
    // Mapea a /login
    // -------------------------------------------------------------------------
    @PostMapping("/login")
    public String authenticate(@ModelAttribute("loginDto") loginDTO loginDto, 
                               HttpSession session, 
                               RedirectAttributes flash) {

        // 1. Llama al método de autenticación del servicio
        user authenticatedUser = servicioUsuario.autenticacion(
            loginDto.getEmailInstitucional(), 
            loginDto.getContrasena()
        );

        if (authenticatedUser != null) {
            
            // 2. Autenticación Exitosa: Guardar el usuario en la sesión
            session.setAttribute("userSession", authenticatedUser); 
            
            String rol = authenticatedUser.getRol().name(); // Asumiendo que getRol() devuelve un Enum y usamos .name()

            // 3. Redirección basada en el rol
            if ("ADMIN".equals(rol)) {
                flash.addFlashAttribute("exito", "Bienvenido, Administrador.");
                return "redirect:/dahsboard"; 
            } else if ("CLIENTE".equals(rol)) {
                flash.addFlashAttribute("exito", "Hola, " + authenticatedUser.getNombre() + ".");
                return "redirect:/productos/catalogo"; 
            }
            
            // Rol no reconocido
            flash.addFlashAttribute("error", "Error de configuración de rol.");
            return "redirect:/login";

        } else {
            // 4. Autenticación Fallida
            flash.addFlashAttribute("error", "Credenciales inválidas. Verifique email y contraseña.");
            return "redirect:/login";
        }
    }
    
    // -------------------------------------------------------------------------
    // 3. GET: Cerrar Sesión (Logout)
    // Mapea a /logout
    // -------------------------------------------------------------------------
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes flash) {
        session.invalidate(); // Invalida la sesión actual
        flash.addFlashAttribute("warning", "Sesión cerrada con éxito.");
        return "redirect:/login"; // Redirige a la página de login
    }
}