package edu.dosw.parcial.validators;

import edu.dosw.parcial.persistence.entities.ProductoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockValidatorTest {

    private final StockValidator validator = new StockValidator();

    private ProductoEntity producto(int stock) {
        ProductoEntity p = new ProductoEntity();
        p.setStock(stock);
        return p;
    }

    @Test
    void hay_suficiente_stock() {
        assertTrue(validator.haySuficienteStock(producto(10), 5));
    }

    @Test
    void stock_exacto() {
        assertTrue(validator.haySuficienteStock(producto(5), 5));
    }

    @Test
    void stock_insuficiente() {
        assertFalse(validator.haySuficienteStock(producto(3), 10));
    }
}
