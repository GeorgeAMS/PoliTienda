package edu.politienda.Models.DAO;

import java.util.List;

import edu.politienda.Models.ENTITY.product;

public interface IProductDao {

    List<product> listar();
    
    product buscarPorId(Long Id);

    void guardar(product producto);

    void eliminar(Long id);

    List<product> buscarPorNombre(String nombre);

    user buscarPorNombreExacto(String nombre);

    
}
