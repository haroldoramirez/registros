package br.com.registros.model.dto;

import br.com.registros.model.enums.StatusRegistro;
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
    private StatusRegistro status;
    private String dataCadastro;

}
