package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.core.exceptions.ProductoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.StockInsuficienteException;
import edu.dosw.parcial.persistence.entities.ProductoEntity;
import edu.dosw.parcial.persistence.repositories.ProductoRepository;
import edu.dosw.parcial.validators.StockValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductoRepository productoRepository;
    private final StockValidator stockValidator;

    public Map<String, ProductoEntity> validarYObtenerProductos(List<ItemPedidoRequest> items) {
        return items.stream().collect(Collectors.toMap(
                ItemPedidoRequest::getProductoId,
                item -> {
                    ProductoEntity producto = productoRepository.findById(item.getProductoId())
                            .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado: " + item.getProductoId()));
                    if (!stockValidator.haySuficienteStock(producto, item.getCantidad()))
                        throw new StockInsuficienteException("Stock insuficiente para: " + item.getProductoId());
                    return producto;
                }
        ));
    }
}
