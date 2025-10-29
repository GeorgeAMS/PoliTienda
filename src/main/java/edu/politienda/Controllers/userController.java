package edu.politienda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.politienda.Models.ENTITY.Rol;
import edu.politienda.Models.ENTITY.user;
import edu.politienda.Services.userService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/Usuarios")
public class userController {
    private final userService servicioControlar;

    public userController(userService servicioControlar){
        this.servicioControlar=servicioControlar;
    }

    @GetMapping
    public String listar(Model model){
        model.addAttribute("Usuarios", servicioControlar.listUsers());
        return "Usuarios"; 
    }

    @GetMapping("/UsuariosNuevos")
    public String nuevo(Model model){
        model.addAttribute("Usuario", new user());
        model.addAttribute("todosLosRoles", Rol.values()); 
        return "formUsuarios";
    }

    @GetMapping("/EditarUsuario/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash){
        var u = servicioControlar.buscarPorId(id);
        if (u == null){
            flash.addFlashAttribute("error", "El Usuario que intentas editar no existe.");
            return "redirect:/Usuarios";
        }
        model.addAttribute("todosLosRoles", Rol.values());   
        model.addAttribute("Usuario", u);
        model.addAttribute("titulo", "Editando Usuario: " + u.getNombre());
        return "formUsuarios";
    }

    

@PostMapping("/GuardarUsuarios")
public String guardar(@Valid @ModelAttribute("Usuario") user u, BindingResult resultado,RedirectAttributes flash, Model model) {
    
    if (resultado.hasErrors()) {
        
        model.addAttribute("todosLosRoles", edu.politienda.Models.ENTITY.Rol.values());
        model.addAttribute("titulo", (u.getIdUsuario() == null) ? "Crear Nuevo Usuario" : "Editando Usuario: " + u.getNombre());
        return "formUsuarios"; 
    }
    

    try {
        servicioControlar.guardarUsuario(u);
        flash.addFlashAttribute("exito", "El usuario ha sido guardado de manera perfecta.");
        return "redirect:/Usuarios";
    } catch (IllegalArgumentException e) {
        flash.addFlashAttribute("error", e.getMessage());
        flash.addFlashAttribute("Usuario", u);
        return "redirect:/Usuarios/UsuariosNuevos";
    }
}

    @GetMapping("/eliminarUsuario/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash){
        // CORREGIDO: Bloque try-catch para estabilidad de la BD
        try {
            servicioControlar.eliminarUsuarios(id);
            flash.addFlashAttribute("warning", "El Usuario fue eliminado.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el usuario. Debe eliminar primero los registros asociados (pedidos, etc.).");
        }
        return "redirect:/Usuarios";
    }

    @GetMapping("/buscarUsuarios")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model model) {
        model.addAttribute("Usuarios", servicioControlar.buscarPorNombre(nombre));
        model.addAttribute("titulo", "Resultados para: " + nombre);
        return "Usuarios";
    }

}