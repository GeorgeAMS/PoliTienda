package edu.politienda.Models.ENTITY;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalles")
public class detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encabezado",nullable = false)
    private encabezado encabezado;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto",nullable = false)
    private product producto;
    private Integer cantidad;
    private BigDecimal valorUnitario;
    private BigDecimal descuento;
    private BigDecimal valorTotalPorLinea;

    public detalle(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public encabezado getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(encabezado encabezado) {
        this.encabezado = encabezado;
    }

    public product getProducto() {
        return producto;
    }

    public void setProducto(product producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getValorTotalPorLinea() {
        return valorTotalPorLinea;
    }

    public void setValorTotalPorLinea(BigDecimal valorTotalPorLinea) {
        this.valorTotalPorLinea = valorTotalPorLinea;
    }
    

}
