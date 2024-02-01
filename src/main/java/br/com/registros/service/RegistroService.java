package br.com.registros.service;

import br.com.registros.model.entity.Registro;

public interface RegistroService {

    Registro salvarRegistro(Registro registro);

    void validarRegistro(Registro registro);

}
