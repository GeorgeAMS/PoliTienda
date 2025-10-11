package edu.politienda.Services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.politienda.Models.DAO.IProductDao;
import edu.politienda.Models.ENTITY.product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Service
public class productService {
    private final IProductDao productDao;

    @PersistenceContext
    private EntityManager em;

    public productService(IProductDao productDao){
        this.productDao = productDao;
    }

   @Transactional(readOnly = true)
   public List<product> listarUsuarios() {
        return productDao.listar();
    }

    @Transactional(readOnly = true)
    public product buscarPorId(Long Id){
        return productDao.buscarPorId(Id);
    }

    @Transactional
    public void guardarProducto(product producto) {
    
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (producto.getMoney() == null || producto.getMoney().compareTo(BigDecimal.ZERO) < 0) { 
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }
        if (producto.getStock() == null || producto.getStock() < 0) { 
            throw new IllegalArgumentException("El stock del producto no puede ser negativo.");
        }
        
        product productoExistente = productDao.buscarPorNombreExacto(producto.getNombre());
        if (productoExistente != null) {
            if (producto.getIdProduct() == null || !productoExistente.getIdProduct().equals(producto.getIdProduct())) {
                throw new IllegalArgumentException("Ya existe un producto con el nombre: " + producto.getNombre());
            }
        }
        
             productDao.guardar(producto); 
        }
        
        @Transactional
        public void eliminarProducto(Long id) {
            productDao.eliminar(id);
        }

        @Transactional
        public List<product> buscarPorNombre(String nombre) {
         return productDao.buscarPorNombre(nombre);
        }

}

