package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<ProductoEntity, String> {
    Optional<ProductoEntity> findByCodigoQR(String codigoQR);
}