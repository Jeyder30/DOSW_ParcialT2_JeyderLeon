package edu.dosw.parcial.controller.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class ErrorResponse {
    private int codigo;
    private String mensaje;
}
