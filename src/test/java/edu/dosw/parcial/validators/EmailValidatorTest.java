package edu.dosw.parcial.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    private final EmailValidator validator = new EmailValidator();

    @Test
    void formato_valido() {
        assertTrue(validator.isFormatoValido("juan@universidad.edu.co"));
    }

    @Test
    void formato_invalido_sin_arroba() {
        assertFalse(validator.isFormatoValido("juanuniversidad.edu.co"));
    }

    @Test
    void formato_invalido_nulo() {
        assertFalse(validator.isFormatoValido(null));
    }

    @Test
    void dominio_institucional_valido() {
        assertTrue(validator.isDominioInstitucional("juan@escuela.edu.co"));
    }

    @Test
    void dominio_institucional_invalido() {
        assertFalse(validator.isDominioInstitucional("juan@gmail.com"));
    }

    @Test
    void dominio_institucional_nulo() {
        assertFalse(validator.isDominioInstitucional(null));
    }
}
