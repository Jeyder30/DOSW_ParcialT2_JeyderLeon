package edu.dosw.parcial.controller.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class LoginResponse {
    private String id;
    private String nombre;
    private String rol;
}
