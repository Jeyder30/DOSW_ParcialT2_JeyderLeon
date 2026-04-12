package edu.dosw.parcial.controller.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter @Builder
@Schema(description = "Información del producto consultado por QR")
public class ProductoResponse {
    @Schema(example = "prod_001")
    private String id;
    @Schema(example = "Café Americano")
    private String nombre;
    @Schema(example = "Café negro 250ml")
    private String descripcion;
    @Schema(example = "2500.00")
    private BigDecimal precio;
    @Schema(example = "QR_CAFE_001")
    private String codigoQR;
    @Schema(example = "50")
    private Integer stock;
    @Schema(example = "disponible")
    private String estado;
}
