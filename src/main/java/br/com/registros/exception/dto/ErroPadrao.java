package br.com.registros.exception.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErroPadrao {
    private int code;
    private String message;
}
