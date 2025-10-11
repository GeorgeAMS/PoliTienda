package edu.politienda.Models.DAO;

import java.util.List;

import edu.politienda.Models.ENTITY.user;

public interface IUserDao {

    List<user> listar();
    
    user buscarPorId(Long Id);

    void guardar(user usuario);

    void eliminar(Long id);

    List<user> buscarPorNombre(String nombre);

    user buscarPorEmail(String email);

    user buscarPorPassword(String password);
}
