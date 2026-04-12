package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoResponse consultarPorQR(String codigoQR) {
        log.info("[PRODUCTO] Consulta por QR: {}", codigoQR);
        return productoRepository.findByCodigoQR(codigoQR)
                .map(p -> {
                    log.info("[PRODUCTO] Producto encontrado: {} - stock: {}", p.getNombre(), p.getStock());
                    return ProductoResponse.builder()
                            .id(p.getId())
                            .nombre(p.getNombre())
                            .descripcion(p.getDescripcion())
                            .precio(p.getPrecio())
                            .codigoQR(p.getCodigoQR())
                            .stock(p.getStock())
                            .estado(p.getEstado().name().toLowerCase())
                            .build();
                })
                .orElseThrow(() -> {
                    log.warn("[PRODUCTO] Producto no encontrado para QR: {}", codigoQR);
                    return new ProductoNoEncontradoException("El producto no se encontró");
                });
    }
}

