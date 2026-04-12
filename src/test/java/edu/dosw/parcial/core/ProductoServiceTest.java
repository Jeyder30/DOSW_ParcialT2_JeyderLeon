package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.core.services.ProductoService;
import edu.dosw.parcial.persistence.entities.EstadoProducto;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private ProductoEntity producto() {
        ProductoEntity p = new ProductoEntity();
        p.setId("prod_001");
        p.setNombre("Cafe Americano");
        p.setDescripcion("Cafe negro 250ml");
        p.setPrecio(new BigDecimal("2500.00"));
        p.setCodigoQR("QR_CAFE_001");
        p.setStock(50);
        p.setEstado(EstadoProducto.DISPONIBLE);
        return p;
    }

    @Test
    void consultarPorQR_exitoso() {
        when(productoRepository.findByCodigoQR(anyString())).thenReturn(Optional.of(producto()));

        ProductoResponse response = productoService.consultarPorQR("QR_CAFE_001");

        assertEquals("prod_001", response.getId());
        assertEquals("Cafe Americano", response.getNombre());
        assertEquals("disponible", response.getEstado());
        assertEquals(50, response.getStock());
    }

    @Test
    void consultarPorQR_no_encontrado() {
        when(productoRepository.findByCodigoQR(anyString())).thenReturn(Optional.empty());

        assertThrows(ProductoNoEncontradoException.class,
                () -> productoService.consultarPorQR("QR_INVALIDO"));
    }
}

