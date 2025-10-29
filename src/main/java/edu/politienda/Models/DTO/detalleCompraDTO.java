package edu.politienda.Models.DTO;

import java.math.BigDecimal;

public class detalleCompraDTO {
    
    
    private Long idProducto;
    private String nombre;
    private Integer cantidad;
    private BigDecimal descuentoAplicado; 

    public detalleCompraDTO() {
    }

    
    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(BigDecimal descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
} 
    

