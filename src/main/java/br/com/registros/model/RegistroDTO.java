package br.com.registros.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroDTO {

    private String titulo;
    private boolean status;

    public Registro converterParaRegistro() {
        Registro registro = new Registro();
        registro.setTitulo(titulo);
        registro.setStatus(status);
        return registro;
    }
}
