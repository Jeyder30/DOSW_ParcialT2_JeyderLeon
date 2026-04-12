package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.core.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Test
    void consultarPorQR_exitoso_retorna_200() throws Exception {
        ProductoResponse response = ProductoResponse.builder()
                .id("prod_001").nombre("Cafe Americano")
                .precio(new BigDecimal("2500.00")).stock(50)
                .estado("disponible").codigoQR("QR_CAFE_001").build();
        when(productoService.consultarPorQR(anyString())).thenReturn(response);

        mockMvc.perform(get("/api/productos/QR_CAFE_001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("prod_001"))
                .andExpect(jsonPath("$.nombre").value("Cafe Americano"));
    }

    @Test
    void consultarPorQR_no_encontrado_retorna_404() throws Exception {
        when(productoService.consultarPorQR(anyString()))
                .thenThrow(new ProductoNoEncontradoException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/QR_INVALIDO")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Producto no encontrado"));
    }
}

