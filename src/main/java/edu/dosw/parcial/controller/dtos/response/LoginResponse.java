package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
@Schema(description = "Respuesta tras iniciar sesión")
public class LoginResponse {
    @Schema(example = "usr_a1b2c3d4")
    private String id;
    @Schema(example = "Juan Pérez")
    private String nombre;
    @Schema(example = "cliente")
    private String rol;
}
