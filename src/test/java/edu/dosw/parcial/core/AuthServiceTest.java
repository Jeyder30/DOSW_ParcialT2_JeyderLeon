package edu.dosw.parcial.core;

import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.services.AuthService;
import edu.dosw.parcial.persistence.entities.Rol;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import edu.dosw.parcial.utils.HashUtil;
import edu.dosw.parcial.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private HashUtil hashUtil;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private UsuarioEntity usuario() {
        UsuarioEntity u = new UsuarioEntity();
        u.setId("usr_123");
        u.setNombre("Juan Perez");
        u.setCorreo("juan@universidad.edu.co");
        u.setContrasena("hashed");
        u.setRol(Rol.CLIENTE);
        return u;
    }

    private LoginRequest request() {
        LoginRequest r = new LoginRequest();
        r.setCorreo("juan@universidad.edu.co");
        r.setContrasena("Pass123!");
        return r;
    }

    @Test
    void login_exitoso() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario()));
        when(hashUtil.matches(anyString(), anyString())).thenReturn(true);

        LoginResponse response = authService.login(request());

        assertEquals("usr_123", response.getId());
        assertEquals("cliente", response.getRol());
    }

    @Test
    void login_usuario_no_existe() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.empty());

        assertThrows(DatosInvalidosException.class, () -> authService.login(request()));
    }

    @Test
    void login_contrasena_incorrecta() {
        when(usuarioRepository.findByCorreo(anyString())).thenReturn(Optional.of(usuario()));
        when(hashUtil.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(DatosInvalidosException.class, () -> authService.login(request()));
    }
}
