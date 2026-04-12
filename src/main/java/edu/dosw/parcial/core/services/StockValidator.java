package edu.dosw.parcial.core.services;

import org.springframework.stereotype.Component;

@Component
public class StockValidator {

    public boolean haySuficienteStock(ProductoEntity producto, int cantidadSolicitada) {
        return producto.getStock() >= cantidadSolicitada;
    }
}
