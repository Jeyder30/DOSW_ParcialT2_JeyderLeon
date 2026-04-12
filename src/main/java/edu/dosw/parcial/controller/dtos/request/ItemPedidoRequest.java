package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Item individual del pedido")
public class ItemPedidoRequest {

    @NotBlank(message = "El productoId es obligatorio")
    @Schema(description = "ID del producto", example = "prod_001")
    private String productoId;

    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    @Schema(description = "Cantidad solicitada", example = "2")
    private int cantidad;
}

