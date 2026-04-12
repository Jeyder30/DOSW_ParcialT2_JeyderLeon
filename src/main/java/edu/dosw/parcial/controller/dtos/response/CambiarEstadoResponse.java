package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter @Builder
@Schema(description = "Respuesta tras cambiar el estado del pedido")
public class CambiarEstadoResponse {
    @Schema(example = "ped_a1b2c3d4")
    private String id;
    @Schema(example = "EN_PREPARACION")
    private String estado;
    private LocalDateTime fechaActualizacion;
}