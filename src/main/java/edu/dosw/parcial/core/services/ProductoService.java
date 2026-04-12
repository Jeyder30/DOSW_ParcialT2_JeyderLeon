package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoResponse consultarPorQR(String codigoQR) {
        return productoRepository.findByCodigoQR(codigoQR)
                .map(p -> ProductoResponse.builder()
                        .id(p.getId())
                        .nombre(p.getNombre())
                        .descripcion(p.getDescripcion())
                        .precio(p.getPrecio())
                        .codigoQR(p.getCodigoQR())
                        .stock(p.getStock())
                        .estado(p.getEstado().name().toLowerCase())
                        .build())
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto no se encontró"));
    }
}
