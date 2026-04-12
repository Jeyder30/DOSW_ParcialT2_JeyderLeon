package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.exceptions.PedidoActivoException;
import edu.dosw.parcial.core.services.PedidoService;
import edu.dosw.parcial.core.services.StockService;
import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.entities.ItemPedidoEntity;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import edu.dosw.parcial.validators.PedidoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private ProductoRepository productoRepository;
    @Mock private StockService stockService;
    @Mock private PedidoValidator pedidoValidator;

    @InjectMocks
    private PedidoService pedidoService;

    private ProductoEntity producto() {
        ProductoEntity p = new ProductoEntity();
        p.setId("prod_001");
        p.setNombre("Cafe");
        p.setPrecio(new BigDecimal("2500.00"));
        p.setStock(10);
        return p;
    }

    private CrearPedidoRequest crearRequest() {
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProductoId("prod_001");
        item.setCantidad(2);
        CrearPedidoRequest r = new CrearPedidoRequest();
        r.setProductos(List.of(item));
        return r;
    }

    private PedidoEntity pedidoCreado() {
        PedidoEntity p = new PedidoEntity();
        p.setId("ped_001");
        p.setUsuarioId("usr_001");
        p.setEstado(EstadoPedido.CREADO);
        p.setTotal(new BigDecimal("5000.00"));
        p.setFechaCreacion(LocalDateTime.now());
        p.setItems(new ArrayList<>());
        return p;
    }

    @Test
    void crearPedido_exitoso() {
        when(pedidoValidator.tienesPedidoActivo(anyString())).thenReturn(false);
        when(stockService.validarYObtenerProductos(any())).thenReturn(Map.of("prod_001", producto()));
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PedidoResponse response = pedidoService.crearPedido("usr_001", crearRequest());

        assertEquals("CREADO", response.getEstado());
        assertEquals("usr_001", response.getUsuarioId());
        assertEquals(new BigDecimal("5000.00"), response.getTotal());
    }

    @Test
    void crearPedido_con_pedido_activo() {
        when(pedidoValidator.tienesPedidoActivo(anyString())).thenReturn(true);

        assertThrows(PedidoActivoException.class,
                () -> pedidoService.crearPedido("usr_001", crearRequest()));
    }

    @Test
    void cambiarEstado_a_en_preparacion() {
        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("EN_PREPARACION");
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoCreado()));
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CambiarEstadoResponse response = pedidoService.cambiarEstado("ped_001", "usr_001", "CAFETERIA", req);

        assertEquals("EN_PREPARACION", response.getEstado());
    }

    @Test
    void cambiarEstado_cancelar_por_cliente() {
        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("CANCELADO");
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoCreado()));
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CambiarEstadoResponse response = pedidoService.cambiarEstado("ped_001", "usr_001", "CLIENTE", req);

        assertEquals("CANCELADO", response.getEstado());
    }

    @Test
    void cambiarEstado_cliente_no_puede_poner_en_preparacion() {
        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("EN_PREPARACION");
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoCreado()));

        assertThrows(DatosInvalidosException.class,
                () -> pedidoService.cambiarEstado("ped_001", "usr_001", "CLIENTE", req));
    }

    @Test
    void cambiarEstado_pedido_no_existe() {
        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("EN_PREPARACION");
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(DatosInvalidosException.class,
                () -> pedidoService.cambiarEstado("ped_999", "usr_001", "CAFETERIA", req));
    }

    @Test
    void cambiarEstado_estado_invalido() {
        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("ESTADO_RARO");
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedidoCreado()));

        assertThrows(DatosInvalidosException.class,
                () -> pedidoService.cambiarEstado("ped_001", "usr_001", "CAFETERIA", req));
    }

    @Test
    void cambiarEstado_a_entregado_descuenta_stock() {
        PedidoEntity pedido = pedidoCreado();
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        ProductoEntity producto = new ProductoEntity();
        producto.setId("prod_001");
        producto.setStock(10);

        ItemPedidoEntity item = new ItemPedidoEntity();
        item.setProducto(producto);
        item.setCantidad(2);
        pedido.setItems(List.of(item));

        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("ENTREGADO");

        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedido));
        when(productoRepository.findById(anyString())).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CambiarEstadoResponse response = pedidoService.cambiarEstado("ped_001", "usr_001", "CAFETERIA", req);

        assertEquals("ENTREGADO", response.getEstado());
        verify(productoRepository, times(1)).save(any());
    }

    @Test
    void cambiarEstado_cancelar_pedido_de_otro_usuario_falla() {
        PedidoEntity pedido = pedidoCreado();
        pedido.setUsuarioId("usr_otro");

        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("CANCELADO");
        when(pedidoRepository.findById(anyString())).thenReturn(Optional.of(pedido));

        assertThrows(DatosInvalidosException.class,
                () -> pedidoService.cambiarEstado("ped_001", "usr_001", "CLIENTE", req));
    }
}

