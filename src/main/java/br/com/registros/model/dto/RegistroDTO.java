package br.com.registros.model.dto;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.enums.StatusRegistro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroDTO {

    private String titulo;
    private StatusRegistro status;
    private double latitude;
    private double longitude;

    public Registro converterParaRegistro() {
        Registro registro = new Registro();
        registro.setTitulo(titulo);
        registro.setStatus(status);
        registro.setLatitude(latitude);
        registro.setLongitude(longitude);
        return registro;
    }
}
