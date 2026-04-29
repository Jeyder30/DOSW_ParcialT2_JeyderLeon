package edu.dosw.parcial.core.pedido.application.ports.out;

import edu.dosw.parcial.controller.dtos.request.ItemPedidoRequest;
import edu.dosw.parcial.persistence.entities.ProductoEntity;

import java.util.List;
import java.util.Map;

public interface ProductoPort {
    Map<String, ProductoEntity> validarYObtenerProductos(List<ItemPedidoRequest> items);
    ProductoEntity buscarPorId(String productoId);
    ProductoEntity guardar(ProductoEntity producto);
}
