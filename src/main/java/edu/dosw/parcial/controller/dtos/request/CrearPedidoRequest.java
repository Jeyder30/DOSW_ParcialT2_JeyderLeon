package edu.dosw.parcial.controller.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CrearPedidoRequest {

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    @Valid
    private List<ItemPedidoRequest> productos;
}
