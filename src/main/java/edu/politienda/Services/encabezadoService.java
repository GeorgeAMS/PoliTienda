package edu.politienda.Services;

import java.math.BigDecimal;
import java.math.RoundingMode; // Importación necesaria para manejar el redondeo
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.politienda.Models.DAO.IEncabezadoDao;
import edu.politienda.Models.DAO.IProductDao;
import edu.politienda.Models.DAO.IUserDao;
import edu.politienda.Models.DTO.detalleCompraDTO;
import edu.politienda.Models.ENTITY.detalle;
import edu.politienda.Models.ENTITY.encabezado;
import edu.politienda.Models.ENTITY.product;
import edu.politienda.Models.ENTITY.user;
import jakarta.transaction.Transactional;

@Service
public class encabezadoService {
    
    private final IEncabezadoDao encabezadoDao;
    private final IProductDao productoDao;
    private final IUserDao userDao;


    private static final BigDecimal DIEZ_PORCIENTO = new BigDecimal("0.10");
    
    private static final BigDecimal CERO = BigDecimal.ZERO;
    
    private static final int ESCALA_REDONDEO = 2;


    public encabezadoService(IEncabezadoDao encabezadoDao, IProductDao productoDao, IUserDao userDao) {
        this.encabezadoDao = encabezadoDao;
        this.productoDao = productoDao;
        this.userDao = userDao;
    }

    @Transactional
    public encabezado generarFactura(Long idCliente, List<detalleCompraDTO> itemsComprados) {
        
        user cliente = userDao.buscarPorId(idCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente ID " + idCliente + " no encontrado. Venta cancelada.");
        }
        
        encabezado encabezado = new encabezado();
        encabezado.setCliente(cliente);
        encabezado.setFecha(LocalDateTime.now());
        
        List<detalle> detallesDeFactura = new ArrayList<>();
        BigDecimal subTotalVenta = CERO;
        BigDecimal descuentoTotalVenta = CERO;

        for (detalleCompraDTO itemDto : itemsComprados) {
            
            product p = productoDao.buscarPorId(itemDto.getIdProducto());
            if (p == null) {
                throw new IllegalArgumentException("Producto ID " + itemDto.getIdProducto() + " no existe.");
            }

            // Validar Stock
            Integer cantidad = itemDto.getCantidad();
            if (p.getStock() < cantidad) {
                throw new IllegalArgumentException("Stock insuficiente para el producto: " + p.getNombre() + ". Stock disponible: " + p.getStock());
            }

            
            BigDecimal valorUnitario = p.getMoney();
            
            BigDecimal descuentoManual = itemDto.getDescuentoAplicado() != null ? itemDto.getDescuentoAplicado() : CERO;
            
        
            BigDecimal descuentoPorVolumen = CERO;
            if (cantidad >= 10) {
                descuentoPorVolumen = DIEZ_PORCIENTO;
            }

            BigDecimal descuentoFinal = descuentoManual.max(descuentoPorVolumen); 
            // Convertir cantidad (Integer) a BigDecimal
            BigDecimal cantidadBD = BigDecimal.valueOf(cantidad);
            
            // Cálculo Bruto: valorUnitario * cantidad
            BigDecimal valorLineaBruto = valorUnitario.multiply(cantidadBD)
                .setScale(ESCALA_REDONDEO, RoundingMode.HALF_UP);
            
            // Cálculo del Descuento: valorLineaBruto * descuentoFinal
            BigDecimal valorDescuentoLinea = valorLineaBruto
                .multiply(descuentoFinal)
                .setScale(ESCALA_REDONDEO, RoundingMode.HALF_UP);
                
            BigDecimal valorLineaNeto = valorLineaBruto.subtract(valorDescuentoLinea);
            
            
            detalle detalle = new detalle();
            detalle.setEncabezado(encabezado);
            detalle.setProducto(p);
            detalle.setCantidad(cantidad);
            
            
            detalle.setValorUnitario(valorUnitario); 
            detalle.setDescuento(descuentoFinal); 
            detalle.setValorTotalPorLinea(valorLineaNeto);
            
            detallesDeFactura.add(detalle);
            subTotalVenta = subTotalVenta.add(valorLineaBruto);
            descuentoTotalVenta = descuentoTotalVenta.add(valorDescuentoLinea);
            
            
            p.setStock(p.getStock() - cantidad);
            productoDao.guardar(p);
        }

       
        encabezado.setDetalles(detallesDeFactura);
        encabezado.setSubtotal(subTotalVenta);
        encabezado.setDescuentoTotal(descuentoTotalVenta);
        
       
        BigDecimal totalFinal = subTotalVenta.subtract(descuentoTotalVenta);
        encabezado.setTotal(totalFinal);
        
        return encabezadoDao.guardar(encabezado);
    }


}