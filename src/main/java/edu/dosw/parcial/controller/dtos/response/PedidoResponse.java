package edu.dosw.parcial.controller.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PedidoResponse {
    private String id;
    private String usuarioId;
    private List<ItemPedidoResponse> productos;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fechaCreacion;
}
