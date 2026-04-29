package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.CambiarEstadoRequest;
import edu.dosw.parcial.controller.dtos.request.CrearPedidoRequest;
import edu.dosw.parcial.controller.dtos.response.CambiarEstadoResponse;
import edu.dosw.parcial.controller.dtos.response.PedidoResponse;
import edu.dosw.parcial.core.pedido.application.usecase.CambiarEstadoPedidoUseCase;
import edu.dosw.parcial.core.pedido.application.usecase.CrearPedidoUseCase;
import edu.dosw.parcial.core.pedido.domain.PedidoDomainService;
import edu.dosw.parcial.infrastructure.adapters.out.PedidoRepositoryAdapter;
import edu.dosw.parcial.infrastructure.adapters.out.ProductoPortAdapter;
import edu.dosw.parcial.persistence.repositories.PedidoRepository;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import edu.dosw.parcial.validators.PedidoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final StockService stockService;
    private final PedidoValidator pedidoValidator;
    private final PedidoDomainService pedidoDomainService = new PedidoDomainService();

    @Transactional
    public PedidoResponse crearPedido(String usuarioId, CrearPedidoRequest request) {
        CrearPedidoUseCase useCase = new CrearPedidoUseCase(
                new PedidoRepositoryAdapter(pedidoRepository, pedidoValidator),
                new ProductoPortAdapter(stockService, productoRepository),
                pedidoDomainService
        );
        return useCase.ejecutar(usuarioId, request);
    }

    @Transactional
    public CambiarEstadoResponse cambiarEstado(String pedidoId, String usuarioId,
                                               String rol, CambiarEstadoRequest request) {
        CambiarEstadoPedidoUseCase useCase = new CambiarEstadoPedidoUseCase(
                new PedidoRepositoryAdapter(pedidoRepository, pedidoValidator),
                new ProductoPortAdapter(stockService, productoRepository),
                pedidoDomainService
        );
        return useCase.ejecutar(pedidoId, usuarioId, rol, request);
    }
}
