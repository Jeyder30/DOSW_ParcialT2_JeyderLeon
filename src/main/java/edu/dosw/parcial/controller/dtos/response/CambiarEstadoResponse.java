package edu.dosw.parcial.controller.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CambiarEstadoResponse {
    private String id;
    private String estado;
    private LocalDateTime fechaActualizacion;
}
