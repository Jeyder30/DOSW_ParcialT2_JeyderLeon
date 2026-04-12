package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.EstadoPedido;
import edu.dosw.parcial.persistence.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<PedidoEntity, String> {
    Optional<PedidoEntity> findByUsuarioIdAndEstadoIn(String usuarioId, List<EstadoPedido> estados);
}
