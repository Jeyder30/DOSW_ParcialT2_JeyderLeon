package edu.dosw.parcial.controller.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter @Builder
public class ItemPedidoResponse {
    private String productoId;
    private String nombre;
    private int cantidad;
    private BigDecimal precio;
}
