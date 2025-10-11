package edu.politienda.Models.ENTITY;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Productos")
public class product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long idProduct;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 4, message = "El nombre debe tener al menos 4 caracteres.")
    private String nombre;

    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    @Column(nullable = false)
    private Integer stock;

    @NotNull(message = "El valor o precio es obligatorio.")
    @DecimalMin(value = "0.00", inclusive = true, message = "El precio no puede ser negativo") 
    @Digits(integer = 10, fraction = 2, message = "Formato de precio inválido (máx 10 enteros y 2 decimales)")
    @Column(nullable = false, precision = 12, scale = 2) 
    private BigDecimal money;


    public product() { 

    }

    public product(long idProduct, String nombre, Integer stock, BigDecimal money) {
    
    this.idProduct = idProduct;
    this.nombre = nombre;
    this.stock = stock;
    this.money = money;
    }

    public long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(long idProduct) {
        this.idProduct = idProduct;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    

}
