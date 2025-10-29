package edu.politienda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import edu.politienda.Services.carritoService;

@Controller
@RequestMapping("/carrito")

    public class carritoController {
        private final carritoService carritoService;

    public carritoController(carritoService carritoService) {
            this.carritoService = carritoService;
    }
 @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long idProducto,@RequestParam int cantidad,@RequestParam String nombre,RedirectAttributes flash) {
        carritoService.agregarProductos(idProducto, cantidad, nombre);
        
        flash.addFlashAttribute("success", "Producto agregado. Productos en carrito: " + carritoService.getTotalItems());
        
        
        return "redirect:/productos/catalogo"; 
    }
   

    @GetMapping
    public String verCarrito(Model model) {
        model.addAttribute("carritoItems", carritoService.getItems());
        
        return "carrito"; 
    }

        @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam Long idProducto, RedirectAttributes flash) {
        carritoService.eliminarProducto(idProducto);
        flash.addFlashAttribute("warning", "Producto eliminado. Total: " + carritoService.getTotalItems());
        return "redirect:/carrito"; 
    }
}
