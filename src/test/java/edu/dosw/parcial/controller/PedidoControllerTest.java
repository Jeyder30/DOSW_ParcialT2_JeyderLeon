package edu.dosw.parcial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.exceptions.PedidoActivoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService pedidoService;

    private CrearPedidoRequest crearRequest() {
        ItemPedidoRequest item = new ItemPedidoRequest();
        item.setProductoId("prod_001");
        item.setCantidad(2);
        CrearPedidoRequest r = new CrearPedidoRequest();
        r.setProductos(List.of(item));
        return r;
    }

    @Test
    void crearPedido_exitoso_retorna_201() throws Exception {
        PedidoResponse response = PedidoResponse.builder()
                .id("ped_001").usuarioId("usr_001")
                .total(new BigDecimal("5000.00")).estado("CREADO")
                .fechaCreacion(LocalDateTime.now()).productos(List.of()).build();
        when(pedidoService.crearPedido(anyString(), any())).thenReturn(response);

        mockMvc.perform(post("/api/pedidos")
                        .header("X-Usuario-Id", "usr_001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ped_001"))
                .andExpect(jsonPath("$.estado").value("CREADO"));
    }

    @Test
    void crearPedido_pedido_activo_retorna_409() throws Exception {
        when(pedidoService.crearPedido(anyString(), any()))
                .thenThrow(new PedidoActivoException("Tienes un pedido activo"));

        mockMvc.perform(post("/api/pedidos")
                        .header("X-Usuario-Id", "usr_001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Tienes un pedido activo"));
    }

    @Test
    void cambiarEstado_exitoso_retorna_200() throws Exception {
        CambiarEstadoResponse response = CambiarEstadoResponse.builder()
                .id("ped_001").estado("EN_PREPARACION")
                .fechaActualizacion(LocalDateTime.now()).build();
        when(pedidoService.cambiarEstado(anyString(), anyString(), anyString(), any()))
                .thenReturn(response);

        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("EN_PREPARACION");

        mockMvc.perform(patch("/api/pedidos/ped_001")
                        .header("X-Usuario-Id", "usr_001")
                        .header("X-Usuario-Rol", "SEÑORA_CAFETERIA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PREPARACION"));
    }

    @Test
    void cambiarEstado_invalido_retorna_400() throws Exception {
        when(pedidoService.cambiarEstado(anyString(), anyString(), anyString(), any()))
                .thenThrow(new DatosInvalidosException("El estado del pedido no se puede actualizar"));

        CambiarEstadoRequest req = new CambiarEstadoRequest();
        req.setEstado("CANCELADO");

        mockMvc.perform(patch("/api/pedidos/ped_001")
                        .header("X-Usuario-Id", "usr_001")
                        .header("X-Usuario-Rol", "CLIENTE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El estado del pedido no se puede actualizar"));
    }
}

