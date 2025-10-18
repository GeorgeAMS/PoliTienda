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

    // Constante para el descuento por volumen (10%)
    private static final BigDecimal DIEZ_PORCIENTO = new BigDecimal("0.10");
    // Constante para el valor neutro (cero)
    private static final BigDecimal CERO = BigDecimal.ZERO;
    // Constante para la escala de redondeo (2 decimales)
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

            // 1. Obtener y preparar datos
            BigDecimal valorUnitario = p.getMoney();
            // Descuento manual: usa CERO si es nulo
            BigDecimal descuentoManual = itemDto.getDescuentoAplicado() != null ? itemDto.getDescuentoAplicado() : CERO;
            
            // 2. Regla de Descuento por Volumen
            BigDecimal descuentoPorVolumen = CERO;
            if (cantidad >= 10) {
                descuentoPorVolumen = DIEZ_PORCIENTO;
            }

            // 3. Obtener el descuento final (usando .max() de BigDecimal)
            // Se aplica el descuento más alto entre el manual y el de volumen
            BigDecimal descuentoFinal = descuentoManual.max(descuentoPorVolumen); 
            
            // 4. Cálculos con BigDecimal
            
            // Convertir cantidad (Integer) a BigDecimal
            BigDecimal cantidadBD = BigDecimal.valueOf(cantidad);
            
            // Cálculo Bruto: valorUnitario * cantidad
            BigDecimal valorLineaBruto = valorUnitario.multiply(cantidadBD)
                .setScale(ESCALA_REDONDEO, RoundingMode.HALF_UP);
            
            // Cálculo del Descuento: valorLineaBruto * descuentoFinal
            BigDecimal valorDescuentoLinea = valorLineaBruto
                .multiply(descuentoFinal)
                .setScale(ESCALA_REDONDEO, RoundingMode.HALF_UP);
                
            // Cálculo Neto: valorLineaBruto - valorDescuentoLinea
            BigDecimal valorLineaNeto = valorLineaBruto.subtract(valorDescuentoLinea);
            
            // 5. Crear Detalle y Asignar
            detalle detalle = new detalle();
            detalle.setEncabezado(encabezado);
            detalle.setProducto(p);
            detalle.setCantidad(cantidad);
            
            // Asignación de BigDecimal a la entidad Detalle
            detalle.setValorUnitario(valorUnitario); 
            detalle.setDescuento(descuentoFinal); 
            detalle.setValorTotalPorLinea(valorLineaNeto);
            
            detallesDeFactura.add(detalle);
            
            // 6. Acumular Totales Generales (usando .add())
            subTotalVenta = subTotalVenta.add(valorLineaBruto);
            descuentoTotalVenta = descuentoTotalVenta.add(valorDescuentoLinea);
            
            // 7. Actualizar Stock
            p.setStock(p.getStock() - cantidad);
            productoDao.guardar(p);
        }

        // 8. Finalizar Encabezado y Guardar
        encabezado.setDetalles(detallesDeFactura);
        encabezado.setSubtotal(subTotalVenta);
        encabezado.setDescuentoTotal(descuentoTotalVenta);
        
        // Cálculo del Total Final: SubTotal - DescuentoTotal
        BigDecimal totalFinal = subTotalVenta.subtract(descuentoTotalVenta);
        encabezado.setTotal(totalFinal);
        
        return encabezadoDao.guardar(encabezado);
    }
}