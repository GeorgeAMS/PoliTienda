package edu.politienda.Services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import edu.politienda.Models.DTO.detalleCompraDTO;

@Service
@SessionScope
public class carritoService {
    private List<detalleCompraDTO> productos = new ArrayList<>();

    public void agregarProductos(Long idProducto, int cantidad){
        
        boolean productoEncontrado = false;
        
        // 1. BUCLER: Buscamos si el producto YA existe para actualizar la cantidad
        for(detalleCompraDTO item : productos){
            if (item.getIdProducto().equals(idProducto)) {
                item.setCantidad(item.getCantidad() + cantidad);
                productoEncontrado = true;
                break; // Detenemos la búsqueda
            }
        }
        
        // 2. LÓGICA DE ADICIÓN: Solo se ejecuta si NO se encontró el producto en el bucle anterior
        if (!productoEncontrado) {
            detalleCompraDTO newItem = new detalleCompraDTO();
            newItem.setIdProducto(idProducto);
            newItem.setCantidad(cantidad);
            newItem.setDescuentoAplicado(BigDecimal.ZERO); 
            productos.add(newItem); // <-- CORREGIDO: Solo se añade una vez al final
        }
    }

            public List<detalleCompraDTO> getItems() {
                return productos;
            }

            public void limpiarCarrito() {
                productos.clear();
            }
            
            public int getTotalItems() {
                int total = 0;
                for (detalleCompraDTO item : productos) {
                    total += item.getCantidad();
                }
                return total;
            }
}
