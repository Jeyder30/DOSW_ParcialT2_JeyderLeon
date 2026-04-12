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
        Map<String, ProductoEntity> productos = items.stream()
                .map(item -> productoRepository.findById(item.getProductoId())
                        .orElseThrow(() -> new ProductoNoEncontradoException(
                                "El producto no existe: " + item.getProductoId())))
                .collect(Collectors.toMap(ProductoEntity::getId, p -> p));

        List<String> sinStock = items.stream()
                .filter(item -> !stockValidator.haySuficienteStock(
                        productos.get(item.getProductoId()), item.getCantidad()))
                .map(ItemPedidoRequest::getProductoId)
                .toList();

        if (!sinStock.isEmpty()) {
            throw new StockInsuficienteException("No hay suficiente stock de el/los productos: " + sinStock);
        }

        return productos;
    }
}

