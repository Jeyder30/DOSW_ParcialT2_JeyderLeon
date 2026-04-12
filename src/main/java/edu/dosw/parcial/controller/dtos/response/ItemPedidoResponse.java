package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter @Builder
@Schema(description = "Item dentro de la respuesta del pedido")
public class ItemPedidoResponse {
    @Schema(example = "prod_001")
    private String productoId;
    @Schema(example = "Café Americano")
    private String nombre;
    @Schema(example = "2")
    private int cantidad;
    @Schema(example = "2500.00")
    private BigDecimal precio;
}