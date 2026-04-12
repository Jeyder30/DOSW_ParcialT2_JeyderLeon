package edu.dosw.parcial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.parcial.controller.dtos.request.RegistroRequest;
import edu.dosw.parcial.controller.dtos.response.RegistroResponse;
import edu.dosw.parcial.core.exceptions.CorreoYaExisteException;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private RegistroRequest request() {
        RegistroRequest r = new RegistroRequest();
        r.setNombre("Juan Perez");
        r.setCorreo("juan@universidad.edu.co");
        r.setContrasena("Pass123!");
        return r;
    }

    @Test
    void registrar_exitoso_retorna_201() throws Exception {
        RegistroResponse response = RegistroResponse.builder()
                .id("usr_001").nombre("Juan Perez")
                .correo("juan@universidad.edu.co").rol("cliente").build();
        when(usuarioService.registrar(any())).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("usr_001"))
                .andExpect(jsonPath("$.rol").value("cliente"));
    }

    @Test
    void registrar_correo_duplicado_retorna_409() throws Exception {
        when(usuarioService.registrar(any())).thenThrow(new CorreoYaExisteException("El correo ya existe"));

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("El correo ya existe"));
    }

    @Test
    void registrar_datos_invalidos_retorna_400() throws Exception {
        when(usuarioService.registrar(any())).thenThrow(new DatosInvalidosException("Datos inválidos"));

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Datos inválidos"));
    }
}
