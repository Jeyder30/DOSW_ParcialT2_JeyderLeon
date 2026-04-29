package edu.dosw.parcial.infrastructure.adapters.out;

import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.core.pedido.application.ports.out.ProductoPort;
import edu.dosw.parcial.core.services.StockService;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;

import java.util.List;
import java.util.Map;

public class ProductoPortAdapter implements ProductoPort {

    private final StockService stockService;
    private final ProductoRepository productoRepository;

    public ProductoPortAdapter(StockService stockService, ProductoRepository productoRepository) {
        this.stockService = stockService;
        this.productoRepository = productoRepository;
    }

    @Override
    public Map<String, ProductoEntity> validarYObtenerProductos(List<ItemPedidoRequest> items) {
        return stockService.validarYObtenerProductos(items);
    }

    @Override
    public ProductoEntity buscarPorId(String productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado"));
    }

    @Override
    public ProductoEntity guardar(ProductoEntity producto) {
        return productoRepository.save(producto);
    }
}
