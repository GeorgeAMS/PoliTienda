package edu.politienda.Models.DAO;

import edu.politienda.Models.ENTITY.encabezado;


public interface IEncabezadoDao {

    
encabezado guardar(encabezado encabezado);
encabezado buscarPorId(Long Id);
    
}
