package br.com.registros.model.dto;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.enums.StatusRegistro;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroDTO {

    private String titulo;
    private StatusRegistro status;

    public Registro converterParaRegistro() {
        Registro registro = new Registro();
        registro.setTitulo(titulo);
        registro.setStatus(status);
        return registro;
    }
}
