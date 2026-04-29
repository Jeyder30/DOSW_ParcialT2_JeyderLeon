package edu.dosw.parcial.core.pedido.domain;

import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.ItemPedidoResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.entities.ItemPedidoEntity;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PedidoDomainService {

    private static final Map<EstadoPedido, Set<EstadoPedido>> TRANSICIONES_CAFETERIA = Map.of(
            EstadoPedido.CREADO, Set.of(EstadoPedido.EN_PREPARACION),
            EstadoPedido.EN_PREPARACION, Set.of(EstadoPedido.ENTREGADO)
    );

    public PedidoEntity crearPedido(String usuarioId, CrearPedidoRequest request, Map<String, ProductoEntity> productos) {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId("ped_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        pedido.setUsuarioId(usuarioId);
        pedido.setEstado(EstadoPedido.CREADO);
        pedido.setFechaCreacion(LocalDateTime.now());

        List<ItemPedidoEntity> items = request.getProductos().stream()
                .map(itemReq -> construirItem(itemReq, pedido, productos))
                .toList();

        pedido.setItems(items);
        pedido.setTotal(calcularTotal(items));
        return pedido;
    }

    public EstadoPedido parsearEstado(String estado) {
        try {
            return EstadoPedido.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DatosInvalidosException("Estado inválido: " + estado);
        }
    }

    public void validarTransicion(PedidoEntity pedido, EstadoPedido nuevo, String rol, String usuarioId) {
        boolean esCafeteria = "CAFETERIA".equals(rol);

        if (nuevo == EstadoPedido.CANCELADO) {
            if (!"CLIENTE".equals(rol) || !pedido.getUsuarioId().equals(usuarioId) ||
                    pedido.getEstado() != EstadoPedido.CREADO) {
                throw new DatosInvalidosException("El estado del pedido no se puede actualizar");
            }
            return;
        }

        if (!esCafeteria || !TRANSICIONES_CAFETERIA.getOrDefault(pedido.getEstado(), Set.of()).contains(nuevo)) {
            throw new DatosInvalidosException("El estado del pedido no se puede actualizar");
        }
    }

    public PedidoResponse construirRespuestaPedido(PedidoEntity pedido) {
        List<ItemPedidoResponse> itemsResp = pedido.getItems().stream()
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

    public CambiarEstadoResponse construirRespuestaCambioEstado(PedidoEntity pedido) {
        return CambiarEstadoResponse.builder()
                .id(pedido.getId())
                .estado(pedido.getEstado().name())
                .fechaActualizacion(pedido.getFechaActualizacion())
                .build();
    }

    private ItemPedidoEntity construirItem(ItemPedidoRequest req, PedidoEntity pedido, Map<String, ProductoEntity> productos) {
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
}
