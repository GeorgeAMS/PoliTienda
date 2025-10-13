package edu.politienda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.politienda.Models.ENTITY.product;
import edu.politienda.Services.productService;

@Controller
@RequestMapping("/productos")
public class productController {

    private final productService servicioControlar;

    public productController(productService servicioControlar){
        this.servicioControlar = servicioControlar;
    }

    // Listado principal
    @GetMapping
    public String listar(Model model){
        model.addAttribute("productos", servicioControlar.listaProducts());
        return "productos"; // <-- corresponde a productos.html
    }

    // Formulario nuevo
    @GetMapping("/ProductoNuevo")
    public String nuevo(Model model){
        model.addAttribute("producto", new product());
        return "formProduct";
    }

    // Formulario editar
    @GetMapping("/EditarProducto/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash){
        var p = servicioControlar.buscarPorId(id);
        if (p == null){
            flash.addFlashAttribute("error", "El producto que intentas editar no existe.");
            return "redirect:/productos";
        }
        model.addAttribute("producto", p);
        model.addAttribute("titulo", "Editando Producto: " + p.getNombre());
        return "formProduct";
    }

    // Guardar (crear/actualizar)
    @PostMapping("/GuardarProducto")
    public String guardar(@ModelAttribute product p, RedirectAttributes flash){
        try {
            servicioControlar.guardarProducto(p);
            flash.addFlashAttribute("exito", "El producto ha sido guardado de manera perfecta.");
            return "redirect:/productos";
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            flash.addFlashAttribute("producto", p);
            return "redirect:/productos/ProductoNuevo";
        }
    }

    // Eliminar
    @GetMapping("/eliminarProducto/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash){
        servicioControlar.eliminarProducto(id);
        flash.addFlashAttribute("warning", "El producto fue eliminado.");
        return "redirect:/productos";
    }

    // Buscar por nombre
    @GetMapping("/buscarProducto")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model model) {
        model.addAttribute("productos", servicioControlar.buscarPorNombre(nombre));
        model.addAttribute("titulo", "Resultados para: " + nombre);
        return "productos";
    }
}
