package edu.dosw.parcial.controller.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarEstadoRequest {

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
