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

    
    @GetMapping({"/","/login"})
    public String showLoginForm(Model model) {
        model.addAttribute("loginDto", new loginDTO()); 
        return "login"; 
    }


    @PostMapping("/login")
    public String authenticate(@ModelAttribute("loginDto") loginDTO loginDto,HttpSession session,  RedirectAttributes flash) {

    
        user authenticatedUser = servicioUsuario.autenticacion(loginDto.getEmailInstitucional(), loginDto.getContrasena()
        );

        if (authenticatedUser != null) {
            
            session.setAttribute("userSession", authenticatedUser); 
            
            String rol = authenticatedUser.getRol().name(); 

        
            if ("ADMIN".equals(rol)) {
                flash.addFlashAttribute("exito", "Bienvenido, Administrador.");
                return "redirect:/dahsboard"; 
            } else if ("CLIENTE".equals(rol)) {
                flash.addFlashAttribute("exito", "Hola, " + authenticatedUser.getNombre() + ".");
                return "redirect:/productos/catalogo"; 
            }
            
            
            flash.addFlashAttribute("error", "Error de configuración de rol.");
            return "redirect:/login";

        } else {
            
            flash.addFlashAttribute("error", "Credenciales inválidas. Verifique email y contraseña.");
            return "redirect:/login";
        }
    }
    
    
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes flash) {
        session.invalidate();
        flash.addFlashAttribute("warning", "Sesión cerrada con éxito.");
        return "redirect:/login"; 
    }
}