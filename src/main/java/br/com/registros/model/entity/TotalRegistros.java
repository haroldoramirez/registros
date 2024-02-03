package br.com.registros.model.entity;

import br.com.registros.model.enums.StatusRegistro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalRegistros {

    private StatusRegistro status;
    private long quantidade;
}
