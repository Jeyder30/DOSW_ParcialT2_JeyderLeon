package edu.dosw.parcial.validators;

import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PedidoValidator {

    private final PedidoRepository pedidoRepository;

    public boolean tienesPedidoActivo(String usuarioId) {
        return pedidoRepository.findByUsuarioIdAndEstadoIn(
                usuarioId,
                List.of(EstadoPedido.CREADO, EstadoPedido.EN_PREPARACION)
        ).isPresent();
    }
}

