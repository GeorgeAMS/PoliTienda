package edu.politienda.Models.DAO;

import org.springframework.stereotype.Repository;

import edu.politienda.Models.ENTITY.encabezado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class EncabezadoDaoImp implements IEncabezadoDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public encabezado guardar(encabezado encabezado) {
       if (encabezado.getId() == null) {
            em.persist(encabezado);
        } else {
            encabezado = em.merge(encabezado);
        }
        return encabezado;
    }
    
}
