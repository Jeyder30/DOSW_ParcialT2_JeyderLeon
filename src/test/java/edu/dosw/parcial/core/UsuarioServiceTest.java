package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.RegistroRequest;
import edu.dosw.parcial.controller.dtos.response.RegistroResponse;
import edu.dosw.parcial.core.exceptions.CorreoYaExisteException;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.services.UsuarioService;
import edu.dosw.parcial.persistence.entities.Rol;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import edu.dosw.parcial.utils.HashUtil;
import edu.dosw.parcial.validators.EmailValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private HashUtil hashUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private RegistroRequest request() {
        RegistroRequest r = new RegistroRequest();
        r.setNombre("Juan Perez");
        r.setCorreo("juan@universidad.edu.co");
        r.setContrasena("Pass123!");
        return r;
    }

    @Test
    void registrar_exitoso() {
        when(emailValidator.isDominioInstitucional(anyString())).thenReturn(true);
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(hashUtil.hash(anyString())).thenReturn("hashed");
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RegistroResponse response = usuarioService.registrar(request());

        assertEquals("Juan Perez", response.getNombre());
        assertEquals("juan@universidad.edu.co", response.getCorreo());
        assertEquals("cliente", response.getRol());
    }

    @Test
    void registrar_dominio_invalido() {
        when(emailValidator.isDominioInstitucional(anyString())).thenReturn(false);

        assertThrows(DatosInvalidosException.class, () -> usuarioService.registrar(request()));
    }

    @Test
    void registrar_contrasena_sin_mayuscula() {
        RegistroRequest r = request();
        r.setContrasena("pass1234");
        when(emailValidator.isDominioInstitucional(anyString())).thenReturn(true);

        assertThrows(DatosInvalidosException.class, () -> usuarioService.registrar(r));
    }

    @Test
    void registrar_contrasena_sin_numero() {
        RegistroRequest r = request();
        r.setContrasena("Password");
        when(emailValidator.isDominioInstitucional(anyString())).thenReturn(true);

        assertThrows(DatosInvalidosException.class, () -> usuarioService.registrar(r));
    }

    @Test
    void registrar_contrasena_muy_corta() {
        RegistroRequest r = request();
        r.setContrasena("P1!");
        when(emailValidator.isDominioInstitucional(anyString())).thenReturn(true);

        assertThrows(DatosInvalidosException.class, () -> usuarioService.registrar(r));
    }

    @Test
    void registrar_correo_duplicado() {
        when(emailValidator.isDominioInstitucional(anyString())).thenReturn(true);
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(true);

        assertThrows(CorreoYaExisteException.class, () -> usuarioService.registrar(request()));
    }
}
