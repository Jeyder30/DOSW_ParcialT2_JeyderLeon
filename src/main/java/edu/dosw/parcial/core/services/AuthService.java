package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import edu.dosw.parcial.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final HashUtil hashUtil;

    public LoginResponse login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new DatosInvalidosException("Credenciales inválidas"));

        if (!hashUtil.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new DatosInvalidosException("Credenciales inválidas");
        }

        return LoginResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol().name().toLowerCase())
                .build();
    }
}
