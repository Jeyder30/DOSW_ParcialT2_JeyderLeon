package edu.dosw.parcial.core.services;

import edu.dosw.parcial.controller.dtos.request.RegistroRequest;
import edu.dosw.parcial.controller.dtos.response.RegistroResponse;
import edu.dosw.parcial.core.exceptions.CorreoYaExisteException;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.persistence.entities.Rol;
import edu.dosw.parcial.persistence.entities.UsuarioEntity;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import edu.dosw.parcial.utils.HashUtil;
import edu.dosw.parcial.validators.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmailValidator emailValidator;
    private final HashUtil hashUtil;

    public RegistroResponse registrar(RegistroRequest request) {
        if (!emailValidator.isDominioInstitucional(request.getCorreo())) {
            throw new DatosInvalidosException("El correo debe pertenecer al dominio institucional (.edu.co)");
        }
        if (!contrasenaValida(request.getContrasena())) {
            throw new DatosInvalidosException("La contraseña debe tener mínimo 8 caracteres, una mayúscula y un número");
        }
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new CorreoYaExisteException("El correo ya existe");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId("usr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        usuario.setNombre(request.getNombre());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(hashUtil.hash(request.getContrasena()));
        usuario.setRol(Rol.CLIENTE);
        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioRepository.save(usuario);

        return RegistroResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name().toLowerCase())
                .build();
    }

    private boolean contrasenaValida(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) return false;
        return contrasena.chars().anyMatch(Character::isUpperCase) &&
               contrasena.chars().anyMatch(Character::isDigit);
    }
}
