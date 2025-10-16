package edu.politienda.Models.ENTITY;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="encabezados")
public class encabezado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_cliente", nullable = false)
    private user cliente;

    private LocalDateTime fecha;
    private Double subtotal;
    private Double total;
    private Double descuentoTotal;

    @OneToMany(mappedBy = "encabezado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<detalle> detalles;

    public encabezado (){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public user getCliente() {
        return cliente;
    }

    public void setCliente(user cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public double getDescuentoTotal() {
        return descuentoTotal;
    }

    public void setDescuentoTotal(Double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public List<detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<detalle> detalles) {
        this.detalles = detalles;
    }
    

}
