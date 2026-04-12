package edu.dosw.parcial.validators;

import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoValidatorTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoValidator pedidoValidator;

    @Test
    void tiene_pedido_activo() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setEstado(EstadoPedido.CREADO);
        when(pedidoRepository.findByUsuarioIdAndEstadoIn(anyString(), any(List.class)))
                .thenReturn(Optional.of(pedido));

        assertTrue(pedidoValidator.tienesPedidoActivo("usr_001"));
    }

    @Test
    void no_tiene_pedido_activo() {
        when(pedidoRepository.findByUsuarioIdAndEstadoIn(anyString(), any(List.class)))
                .thenReturn(Optional.empty());

        assertFalse(pedidoValidator.tienesPedidoActivo("usr_001"));
    }
}

