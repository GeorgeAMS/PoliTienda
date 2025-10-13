package edu.politienda.Services;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.politienda.Models.DAO.IUserDao;
import edu.politienda.Models.ENTITY.user;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class userService {
    private final IUserDao userDao;

    @PersistenceContext
    private EntityManager em;

     public userService(IUserDao userDao){
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
   public List<user> listUsers() {
        return userDao.listar();
    }

    @Transactional(readOnly = true)
    public user buscarPorId(Long Id){
        return userDao.buscarPorId(Id);
    }

    @Transactional
    public void guardarUsuario(user Usuario) {
    
        if (Usuario.getNombre() == null || Usuario.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
         if (Usuario.getEmailInstitucional() == null || Usuario.getEmailInstitucional().trim().isEmpty()) {
        throw new IllegalArgumentException("El email del usuario no puede estar vacío.");
    }
        
        user UsuarioExistente = userDao.buscarPorEmail(Usuario.getEmailInstitucional());
        if (UsuarioExistente != null) {
            if (Usuario.getIdUsuario() == null || !UsuarioExistente.getIdUsuario().equals(Usuario.getIdUsuario())) {
                throw new IllegalArgumentException("Ya existe un usuario registrado con el email: " + Usuario.getEmailInstitucional());
            }
        }
        
             userDao.guardar(Usuario); 
        }

        @Transactional
        public void eliminarProducto(Long id) {
            userDao.eliminar(id);
        }

        @Transactional
        public List<user> buscarPorNombre(String nombre) {
         return userDao.buscarPorNombre(nombre);
        }

         @Transactional
        public user buscarPorEmail(String email) {
         return userDao.buscarPorEmail(email);
        }

        public user buscarPorPassword(String password){
            return userDao.buscarPorPassword(password);
        }

}
