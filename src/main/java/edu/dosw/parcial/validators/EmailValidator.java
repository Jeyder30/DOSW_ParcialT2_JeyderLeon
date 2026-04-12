package edu.dosw.parcial.validators;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final String DOMINIO_INSTITUCIONAL = ".edu.co";

    public boolean isFormatoValido(String correo) {
        return correo != null && EMAIL_PATTERN.matcher(correo).matches();
    }

    public boolean isDominioInstitucional(String correo) {
        return correo != null && correo.endsWith(DOMINIO_INSTITUCIONAL);
    }
}
