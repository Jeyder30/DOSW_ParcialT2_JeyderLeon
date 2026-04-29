package edu.dosw.parcial.core.pedido.application.ports.in;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;

public interface CambiarEstadoPedidoInputPort {
    CambiarEstadoResponse ejecutar(String pedidoId, String usuarioId, String rol, CambiarEstadoRequest request);
}
