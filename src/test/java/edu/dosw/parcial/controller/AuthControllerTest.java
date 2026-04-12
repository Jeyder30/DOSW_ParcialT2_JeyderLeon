package edu.dosw.parcial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.parcial.controller.dtos.request.LoginRequest;
import edu.dosw.parcial.controller.dtos.response.LoginResponse;
import edu.dosw.parcial.core.exceptions.DatosInvalidosException;
import edu.dosw.parcial.core.services.AuthService;
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

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest request() {
        LoginRequest r = new LoginRequest();
        r.setCorreo("juan@universidad.edu.co");
        r.setContrasena("Pass123!");
        return r;
    }

    @Test
    void login_exitoso_retorna_200() throws Exception {
        LoginResponse response = LoginResponse.builder()
                .id("usr_001").nombre("Juan").rol("cliente").build();
        when(authService.login(any())).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("usr_001"))
                .andExpect(jsonPath("$.rol").value("cliente"));
    }

    @Test
    void login_credenciales_invalidas_retorna_400() throws Exception {
        when(authService.login(any())).thenThrow(new DatosInvalidosException("Credenciales incorrectas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Credenciales incorrectas"));
    }
}
