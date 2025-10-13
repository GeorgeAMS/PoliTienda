package edu.politienda.Models.DAO;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.politienda.Models.ENTITY.user;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository

public class UserDaoImp implements IUserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<user> listar() {
        return em.createQuery("from user", user.class ).getResultList();
    }

    @Override
    public user buscarPorId(Long Id) {
        return em.find(user.class,Id);
    }

    @Override
    public void guardar(user usuario) {
        if(usuario.getIdUsuario() != null && usuario.getIdUsuario()> 0){
            em.merge(usuario);
        }else{
            em.persist(usuario);
        }
    }

    @Override
    public void eliminar(Long id) {
        user usuario = buscarPorId(id);
        if(usuario != null){
            em.remove(usuario);
        }
    }

    @Override
    public List<user> buscarPorNombre(String nombre) {
        return em.createQuery("FROM user u WHERE LOWER(u.Nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))", user.class).setParameter("nombre",nombre).getResultList();
        
    }

    @Override
   public user buscarPorEmail(String email) {
        List<user> resultado = em.createQuery("FROM user u WHERE u.emailInstitucional = :email",user.class).setParameter("email", email).getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public user buscarPorPassword(String password) {
        List<user> resultado = em.createQuery("FROM user u WHERE u.contrasena = :password",user.class).setParameter("password", password).getResultList();

        return resultado.isEmpty() ? null : resultado.get(0);
    }
    
}
