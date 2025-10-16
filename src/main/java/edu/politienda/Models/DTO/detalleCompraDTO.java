package edu.politienda.Models.DTO;

public class detalleCompraDTO {
    
    
    private Long idProducto;
    private Integer cantidad;
    private Double descuentoAplicado; 

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

    public Double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(Double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }
} 
    

