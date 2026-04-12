package edu.dosw.parcial.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashUtilTest {

    private final HashUtil hashUtil = new HashUtil();

    @Test
    void hash_genera_valor_diferente_al_original() {
        String hashed = hashUtil.hash("Pass123!");
        assertNotEquals("Pass123!", hashed);
        assertNotNull(hashed);
    }

    @Test
    void matches_correcto() {
        String hashed = hashUtil.hash("Pass123!");
        assertTrue(hashUtil.matches("Pass123!", hashed));
    }

    @Test
    void matches_incorrecto() {
        String hashed = hashUtil.hash("Pass123!");
        assertFalse(hashUtil.matches("OtraPass!", hashed));
    }
}
