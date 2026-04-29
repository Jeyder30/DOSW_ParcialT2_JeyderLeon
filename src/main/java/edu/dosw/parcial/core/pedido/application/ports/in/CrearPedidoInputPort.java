package edu.dosw.parcial.core.pedido.application.ports.in;

import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;

public interface CrearPedidoInputPort {
    PedidoResponse ejecutar(String usuarioId, CrearPedidoRequest request);
}
