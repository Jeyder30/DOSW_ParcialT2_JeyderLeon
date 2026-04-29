package edu.dosw.parcial.core.pedido.application.usecase;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.pedido.application.ports.in.CambiarEstadoPedidoInputPort;
import edu.dosw.parcial.core.pedido.application.ports.out.PedidoRepositoryPort;
import edu.dosw.parcial.core.pedido.application.ports.out.ProductoPort;
import edu.dosw.parcial.core.pedido.domain.PedidoDomainService;
import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;

import java.time.LocalDateTime;

public class CambiarEstadoPedidoUseCase implements CambiarEstadoPedidoInputPort {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProductoPort productoPort;
    private final PedidoDomainService pedidoDomainService;

    public CambiarEstadoPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                      ProductoPort productoPort,
                                      PedidoDomainService pedidoDomainService) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.productoPort = productoPort;
        this.pedidoDomainService = pedidoDomainService;
    }

    @Override
    public CambiarEstadoResponse ejecutar(String pedidoId, String usuarioId, String rol, CambiarEstadoRequest request) {
        PedidoEntity pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new DatosInvalidosException("El pedido no existe"));

        EstadoPedido estadoNuevo = pedidoDomainService.parsearEstado(request.getEstado());
        pedidoDomainService.validarTransicion(pedido, estadoNuevo, rol, usuarioId);

        if (estadoNuevo == EstadoPedido.ENTREGADO) {
            descontarStock(pedido);
        }

        pedido.setEstado(estadoNuevo);
        pedido.setFechaActualizacion(LocalDateTime.now());
        pedidoRepositoryPort.guardar(pedido);
        return pedidoDomainService.construirRespuestaCambioEstado(pedido);
    }

    private void descontarStock(PedidoEntity pedido) {
        pedido.getItems().forEach(item -> {
            ProductoEntity producto = productoPort.buscarPorId(item.getProducto().getId());
            producto.setStock(producto.getStock() - item.getCantidad());
            productoPort.guardar(producto);
        });
    }
}
