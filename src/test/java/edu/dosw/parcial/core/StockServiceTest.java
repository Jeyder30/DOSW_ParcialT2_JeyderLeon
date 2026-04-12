package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.StockInsuficienteException;
import edu.dosw.parcial.core.services.StockService;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import edu.dosw.parcial.validators.StockValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock private StockValidator stockValidator;

    @InjectMocks
    private StockService stockService;

    private ProductoEntity producto() {
        ProductoEntity p = new ProductoEntity();
        p.setId("prod_001");
        p.setNombre("Cafe");
        p.setPrecio(new BigDecimal("2500.00"));
        p.setStock(10);
        return p;
    }

    private ItemPedidoRequest item(int cantidad) {
        ItemPedidoRequest i = new ItemPedidoRequest();
        i.setProductoId("prod_001");
        i.setCantidad(cantidad);
        return i;
    }

    @Test
    void validar_exitoso() {
        when(productoRepository.findById(anyString())).thenReturn(Optional.of(producto()));
        when(stockValidator.haySuficienteStock(any(), anyInt())).thenReturn(true);

        Map<String, ProductoEntity> result = stockService.validarYObtenerProductos(List.of(item(2)));

        assertEquals(1, result.size());
        assertTrue(result.containsKey("prod_001"));
    }

    @Test
    void validar_producto_no_existe() {
        when(productoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class,
                () -> stockService.validarYObtenerProductos(List.of(item(2))));
    }

    @Test
    void validar_stock_insuficiente() {
        when(productoRepository.findById(anyString())).thenReturn(Optional.of(producto()));
        when(stockValidator.haySuficienteStock(any(), anyInt())).thenReturn(false);

        assertThrows(StockInsuficienteException.class,
                () -> stockService.validarYObtenerProductos(List.of(item(20))));
    }
}

