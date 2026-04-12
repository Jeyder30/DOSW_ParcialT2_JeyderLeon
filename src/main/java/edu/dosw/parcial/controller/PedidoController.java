package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.services.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Creación y gestión de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido. Solo CLIENTE.")
    @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PedidoResponse.class)))
    @ApiResponse(responseCode = "400", description = "No hay productos requeridos / No hay suficiente stock",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Tienes un pedido activo",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<PedidoResponse> crearPedido(Authentication auth,
            @Valid @RequestBody CrearPedidoRequest request) {
        String usuarioId = (String) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(usuarioId, request));
    }

    @PatchMapping("/{pedidoId}")
    @Operation(summary = "Cambiar estado del pedido", description = "Actualiza el estado del pedido según el rol del usuario")
    @ApiResponse(responseCode = "200", description = "Estado del pedido actualizado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CambiarEstadoResponse.class)))
    @ApiResponse(responseCode = "400", description = "El estado del pedido no se puede actualizar",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "El rol no es permitido para la acción",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<CambiarEstadoResponse> cambiarEstado(Authentication auth,
            @PathVariable String pedidoId,
            @Valid @RequestBody CambiarEstadoRequest request) {
        String usuarioId = (String) auth.getPrincipal();
        String rol = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.replace("ROLE_", ""))
                .findFirst().orElse("");
        return ResponseEntity.ok(pedidoService.cambiarEstado(pedidoId, usuarioId, rol, request));
    }
}
