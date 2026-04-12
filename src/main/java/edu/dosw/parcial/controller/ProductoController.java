package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.ProductoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import edu.dosw.parcial.core.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Consulta de productos por código QR")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping("/{codigoQR}")
    @Operation(summary = "Consultar producto por QR", description = "Obtiene la información del producto asociado al código QR")
    @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductoResponse.class)))
    @ApiResponse(responseCode = "404", description = "El producto no se encontró",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<ProductoResponse> consultarPorQR(@PathVariable String codigoQR) {
        return ResponseEntity.ok(productoService.consultarPorQR(codigoQR));
    }
}
