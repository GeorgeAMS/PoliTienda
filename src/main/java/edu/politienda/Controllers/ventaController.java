package edu.politienda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.politienda.Services.encabezadoService;
import edu.politienda.Services.carritoService;
import edu.politienda.Services.userService; 
import edu.politienda.Models.ENTITY.user;
import edu.politienda.Models.ENTITY.encabezado;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/venta")
public class ventaController {

    private final encabezadoService encabezadoService;
    private final carritoService carritoService; 
    

    
    public ventaController(encabezadoService encabezadoService, carritoService carritoService, userService userService) {
        this.encabezadoService = encabezadoService;
        this.carritoService = carritoService;
        
    }

    
    @PostMapping("/checkout")
    public String finalizarCompra(HttpSession session, RedirectAttributes flash) {

        
        user clienteEnSesion = (user) session.getAttribute("userSession");
        
        if (clienteEnSesion == null) {
            flash.addFlashAttribute("error", "Debe iniciar sesión para finalizar la compra.");
            return "redirect:/login"; 
        }

        
        var itemsComprados = carritoService.getItems(); 
        
        if (itemsComprados.isEmpty()) {
            flash.addFlashAttribute("error", "El carrito está vacío. Venta no procesada.");
            return "redirect:/carrito"; 
        }
        
       
        try {
            encabezado nuevaFactura = encabezadoService.generarFactura(
                clienteEnSesion.getIdUsuario(), 
                itemsComprados
            );
            
            
            carritoService.limpiarCarrito(); 
            
            
            flash.addFlashAttribute("success", "Factura generada N° " + nuevaFactura.getId() + ". Stock actualizado.");
            return "redirect:/venta/factura/" + nuevaFactura.getId(); 

        } catch (IllegalArgumentException e) {
            
            flash.addFlashAttribute("error", "Error en la venta: " + e.getMessage());
            return "redirect:/carrito";
        }
    }
    
  
    @GetMapping("/factura/{id}")
    public String verFactura(@PathVariable Long id, Model model, RedirectAttributes flash) {
        
        
        encabezado factura = encabezadoService.buscarPorId(id);
        
        if (factura == null) {
            flash.addFlashAttribute("error", "Factura N° " + id + " no encontrada.");
            return "redirect:/"; 
        }
        
        
        model.addAttribute("encabezado", factura);
        
        return "imprimir_factura"; 
    }
}