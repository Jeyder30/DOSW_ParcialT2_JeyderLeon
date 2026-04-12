package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Creación y gestión de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido con los productos escaneados")
    @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PedidoResponse.class)))
    @ApiResponse(responseCode = "400", description = "No hay productos requeridos / No hay suficiente stock")
    @ApiResponse(responseCode = "409", description = "Tienes un pedido activo")
    public ResponseEntity<PedidoResponse> crearPedido(
            @RequestHeader("X-Usuario-Id") String usuarioId,
            @Valid @RequestBody CrearPedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(usuarioId, request));
    }

    @PatchMapping("/{pedidoId}")
    @Operation(summary = "Cambiar estado del pedido", description = "Actualiza el estado del pedido según el rol del usuario")
    @ApiResponse(responseCode = "200", description = "Estado del pedido actualizado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CambiarEstadoResponse.class)))
    @ApiResponse(responseCode = "400", description = "El estado del pedido no se puede actualizar")
    @ApiResponse(responseCode = "403", description = "El rol no es permitido para la acción")
    public ResponseEntity<CambiarEstadoResponse> cambiarEstado(
            @PathVariable String pedidoId,
            @RequestHeader("X-Usuario-Id") String usuarioId,
            @RequestHeader("X-Usuario-Rol") String rol,
            @Valid @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(pedidoId, usuarioId, rol, request));
    }
}
