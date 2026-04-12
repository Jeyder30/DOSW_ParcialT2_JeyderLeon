package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
@Schema(description = "Respuesta tras crear un pedido")
public class PedidoResponse {
    @Schema(example = "ped_a1b2c3d4")
    private String id;
    @Schema(example = "usr_a1b2c3d4")
    private String usuarioId;
    private List<ItemPedidoResponse> productos;
    @Schema(example = "7500.00")
    private BigDecimal total;
    @Schema(example = "CREADO")
    private String estado;
    private LocalDateTime fechaCreacion;
}