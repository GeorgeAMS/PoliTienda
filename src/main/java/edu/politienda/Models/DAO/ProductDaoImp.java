package edu.politienda.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.politienda.Models.ENTITY.product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ProductDaoImp implements IProductDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<product> listar() {
        return em.createQuery("from Productos", product.class).getResultList();
    }

    @Override
    public product buscarPorId(Long Id) {
        return em.find(product.class,Id);
    }


    @Override
    public void guardar(product producto) {
        if (producto.getIdProduct() !=null && producto.getIdProduct() > 0) {
            em.merge(producto); // entonces actualiza
        } else {    
            em.persist(producto); // si no tiene ID, lo inserta nuevo
        }
    }

    @Override
    public void eliminar(Long id) {
       product producto = buscarPorId(id);
        if(producto != null){
            em.remove(producto);
        }
    }

    @Override
    public List<product> buscarPorNombre(String nombre) {
        return em.createQuery("FROM Productos p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))", product.class).setParameter("nombre",nombre).getResultList();
    }

    @Override
    public product buscarPorNombreExacto(String nombre) {
    List<product> resultado = em.createQuery("FROM Product p WHERE LOWER(p.nombre) = LOWER(:nombre)", product.class).setParameter("nombre", nombre).getResultList();

    return resultado.isEmpty() ? null : resultado.get(0);
}
    

}
