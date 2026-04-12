package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Nuevo estado para el pedido")
public class CambiarEstadoRequest {

    @NotBlank(message = "El estado es obligatorio")
    @Schema(description = "Estado destino", example = "EN_PREPARACION", allowableValues = {"EN_PREPARACION", "ENTREGADO", "CANCELADO"})
    private String estado;
}