package edu.dosw.parcial.validators;

import edu.dosw.parcial.persistence.entities.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class StockValidator {

    public boolean haySuficienteStock(ProductoEntity producto, int cantidadSolicitada) {
        return producto.getStock() >= cantidadSolicitada;
    }
}
