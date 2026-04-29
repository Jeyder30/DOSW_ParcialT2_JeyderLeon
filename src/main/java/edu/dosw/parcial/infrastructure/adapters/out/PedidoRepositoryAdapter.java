package edu.dosw.parcial.infrastructure.adapters.out;

import edu.dosw.parcial.core.pedido.application.ports.out.PedidoRepositoryPort;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import edu.dosw.parcial.validators.PedidoValidator;

import java.util.Optional;

public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final PedidoRepository pedidoRepository;
    private final PedidoValidator pedidoValidator;

    public PedidoRepositoryAdapter(PedidoRepository pedidoRepository, PedidoValidator pedidoValidator) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoValidator = pedidoValidator;
    }

    @Override
    public boolean tienePedidoActivo(String usuarioId) {
        return pedidoValidator.tienesPedidoActivo(usuarioId);
    }

    @Override
    public Optional<PedidoEntity> buscarPorId(String pedidoId) {
        return pedidoRepository.findById(pedidoId);
    }

    @Override
    public PedidoEntity guardar(PedidoEntity pedido) {
        return pedidoRepository.save(pedido);
    }
}
