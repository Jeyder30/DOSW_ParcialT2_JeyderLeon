package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Credenciales para iniciar sesión")
public class LoginRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    @Schema(description = "Correo institucional del usuario", example = "juan.perez@universidad.edu.co")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "Pass123!")
    private String contrasena;
}
