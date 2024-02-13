package br.com.registros.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroListDTO {

    private String titulo;
    private String textoStatus;
    private int codigoStatus;
    private String dataCadastro;

}
