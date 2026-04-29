package edu.dosw.parcial.core.pedido.application.ports.out;

import edu.dosw.parcial.persistence.entities.PedidoEntity;

import java.util.Optional;

public interface PedidoRepositoryPort {
    boolean tienePedidoActivo(String usuarioId);
    Optional<PedidoEntity> buscarPorId(String pedidoId);
    PedidoEntity guardar(PedidoEntity pedido);
}
