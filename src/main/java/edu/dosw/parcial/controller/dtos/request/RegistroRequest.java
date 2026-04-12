package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegistroRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    @Schema(description = "Correo institucional (.edu.co)", example = "juan.perez@universidad.edu.co")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Mínimo 8 caracteres, una mayúscula y un número", example = "Pass123!")
    private String contrasena;
}
