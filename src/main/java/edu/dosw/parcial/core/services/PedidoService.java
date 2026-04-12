package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.ItemPedidoResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.exceptions.PedidoActivoException;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.entities.ItemPedidoEntity;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import edu.dosw.parcial.validators.PedidoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private static final Map<EstadoPedido, Set<EstadoPedido>> TRANSICIONES_CAFETERIA = Map.of(
            EstadoPedido.CREADO, Set.of(EstadoPedido.EN_PREPARACION),
            EstadoPedido.EN_PREPARACION, Set.of(EstadoPedido.ENTREGADO)
    );

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final StockService stockService;
    private final PedidoValidator pedidoValidator;

    @Transactional
    public PedidoResponse crearPedido(String usuarioId, CrearPedidoRequest request) {
        if (pedidoValidator.tienesPedidoActivo(usuarioId)) {
            throw new PedidoActivoException("Tienes un pedido activo");
        }

        Map<String, ProductoEntity> productos = stockService.validarYObtenerProductos(request.getProductos());

        PedidoEntity pedido = new PedidoEntity();
        pedido.setId("ped_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        pedido.setUsuarioId(usuarioId);
        pedido.setEstado(EstadoPedido.CREADO);
        pedido.setFechaCreacion(LocalDateTime.now());

        List<ItemPedidoEntity> items = request.getProductos().stream()
                .map(itemReq -> buildItem(itemReq, pedido, productos))
                .toList();

        pedido.setItems(items);
        pedido.setTotal(calcularTotal(items));
        pedidoRepository.save(pedido);

        return buildResponse(pedido, items);
    }

    @Transactional
    public CambiarEstadoResponse cambiarEstado(String pedidoId, String usuarioId,
                                               String rol, CambiarEstadoRequest request) {
        PedidoEntity pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new DatosInvalidosException("El pedido no existe"));

        EstadoPedido estadoNuevo = parsearEstado(request.getEstado());
        validarTransicion(pedido, estadoNuevo, rol, usuarioId);

        if (estadoNuevo == EstadoPedido.ENTREGADO) descontarStock(pedido);

        pedido.setEstado(estadoNuevo);
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedidoRepository.save(pedido);

        return CambiarEstadoResponse.builder()
                .id(pedido.getId())
                .estado(pedido.getEstado().name())
                .fechaActualizacion(pedido.getFechaActualizacion())
                .build();
    }

    private void validarTransicion(PedidoEntity pedido, EstadoPedido nuevo, String rol, String usuarioId) {
        boolean esCafeteria = "CAFETERIA".equals(rol);

        if (nuevo == EstadoPedido.CANCELADO) {
            if (!"CLIENTE".equals(rol) || !pedido.getUsuarioId().equals(usuarioId) ||
                    pedido.getEstado() != EstadoPedido.CREADO)
                throw new DatosInvalidosException("El estado del pedido no se puede actualizar");
            return;
        }

        if (!esCafeteria || !TRANSICIONES_CAFETERIA.getOrDefault(pedido.getEstado(), Set.of()).contains(nuevo))
            throw new DatosInvalidosException("El estado del pedido no se puede actualizar");
    }

    private void descontarStock(PedidoEntity pedido) {
        pedido.getItems().forEach(item -> {
            ProductoEntity producto = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado"));
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        });
    }

    private EstadoPedido parsearEstado(String estado) {
        try {
            return EstadoPedido.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DatosInvalidosException("Estado inválido: " + estado);
        }
    }

    private ItemPedidoEntity buildItem(ItemPedidoRequest req, PedidoEntity pedido,
                                       Map<String, ProductoEntity> productos) {
        ProductoEntity producto = productos.get(req.getProductoId());
        ItemPedidoEntity item = new ItemPedidoEntity();
        item.setId(UUID.randomUUID().toString());
        item.setPedido(pedido);
        item.setProducto(producto);
        item.setCantidad(req.getCantidad());
        item.setPrecioUnitario(producto.getPrecio());
        return item;
    }

    private BigDecimal calcularTotal(List<ItemPedidoEntity> items) {
        return items.stream()
                .map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PedidoResponse buildResponse(PedidoEntity pedido, List<ItemPedidoEntity> items) {
        List<ItemPedidoResponse> itemsResp = items.stream()
                .map(i -> ItemPedidoResponse.builder()
                        .productoId(i.getProducto().getId())
                        .nombre(i.getProducto().getNombre())
                        .cantidad(i.getCantidad())
                        .precio(i.getPrecioUnitario())
                        .build())
                .toList();

        return PedidoResponse.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .productos(itemsResp)
                .total(pedido.getTotal())
                .estado(pedido.getEstado().name())
                .fechaCreacion(pedido.getFechaCreacion())
                .build();
    }
}

@Transactional
public PedidoResponse crearPedido(String usuarioId, CrearPedidoRequest request) {
    if (pedidoValidator.tienesPedidoActivo(usuarioId)) {
        throw new PedidoActivoException("Tienes un pedido activo");
    }

    Map<String, ProductoEntity> productos = stockService.validarYObtenerProductos(request.getProductos());

    PedidoEntity pedido = new PedidoEntity();
    pedido.setId("ped_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
    pedido.setUsuarioId(usuarioId);
    pedido.setEstado(EstadoPedido.CREADO);
    pedido.setFechaCreacion(LocalDateTime.now());

    List<ItemPedidoEntity> items = request.getProductos().stream()
            .map(itemReq -> buildItem(itemReq, pedido, productos))
            .toList();

    pedido.setItems(items);
    pedido.setTotal(calcularTotal(items));

    pedidoRepository.save(pedido);

    return buildResponse(pedido, items);
}

private ItemPedidoEntity buildItem(ItemPedidoRequest req, PedidoEntity pedido,
                                   Map<String, ProductoEntity> productos) {
    ProductoEntity producto = productos.get(req.getProductoId());
    ItemPedidoEntity item = new ItemPedidoEntity();
    item.setId(UUID.randomUUID().toString());
    item.setPedido(pedido);
    item.setProducto(producto);
    item.setCantidad(req.getCantidad());
    item.setPrecioUnitario(producto.getPrecio());
    return item;
}

private BigDecimal calcularTotal(List<ItemPedidoEntity> items) {
    return items.stream()
            .map(i -> i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}

private PedidoResponse buildResponse(PedidoEntity pedido, List<ItemPedidoEntity> items) {
    List<ItemPedidoResponse> itemsResp = items.stream()
            .map(i -> ItemPedidoResponse.builder()
                    .productoId(i.getProducto().getId())
                    .nombre(i.getProducto().getNombre())
                    .cantidad(i.getCantidad())
                    .precio(i.getPrecioUnitario())
                    .build())
            .toList();

    return PedidoResponse.builder()
            .id(pedido.getId())
            .usuarioId(pedido.getUsuarioId())
            .productos(itemsResp)
            .total(pedido.getTotal())
            .estado(pedido.getEstado().name())
            .fechaCreacion(pedido.getFechaCreacion())
            .build();
}
}