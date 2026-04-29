package edu.dosw.parcial.core.pedido.application.usecase;

import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.exceptions.PedidoActivoException;
import edu.dosw.parcial.core.pedido.application.ports.in.CrearPedidoInputPort;
import edu.dosw.parcial.core.pedido.application.ports.out.PedidoRepositoryPort;
import edu.dosw.parcial.core.pedido.application.ports.out.ProductoPort;
import edu.dosw.parcial.core.pedido.domain.PedidoDomainService;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.entities.ProductoEntity;

import java.util.Map;

public class CrearPedidoUseCase implements CrearPedidoInputPort {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProductoPort productoPort;
    private final PedidoDomainService pedidoDomainService;

    public CrearPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                              ProductoPort productoPort,
                              PedidoDomainService pedidoDomainService) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.productoPort = productoPort;
        this.pedidoDomainService = pedidoDomainService;
    }

    @Override
    public PedidoResponse ejecutar(String usuarioId, CrearPedidoRequest request) {
        if (pedidoRepositoryPort.tienePedidoActivo(usuarioId)) {
            throw new PedidoActivoException("Tienes un pedido activo");
        }

        Map<String, ProductoEntity> productos = productoPort.validarYObtenerProductos(request.getProductos());
        PedidoEntity pedido = pedidoDomainService.crearPedido(usuarioId, request, productos);
        pedidoRepositoryPort.guardar(pedido);
        return pedidoDomainService.construirRespuestaPedido(pedido);
    }
}
