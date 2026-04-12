package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@Schema(description = "Respuesta de error estándar")
public class ErrorResponse {
    @Schema(example = "400")
    private int codigo;
    @Schema(example = "Datos inválidos")
    private String mensaje;
}
