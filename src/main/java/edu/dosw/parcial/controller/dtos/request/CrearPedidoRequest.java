package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Schema(description = "Datos para crear un nuevo pedido")
public class CrearPedidoRequest {

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    @Valid
    @Schema(description = "Lista de productos con sus cantidades")
    private List<ItemPedidoRequest> productos;
}
