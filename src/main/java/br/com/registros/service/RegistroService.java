package br.com.registros.service;

import br.com.registros.model.entity.Registro;

import java.util.List;

public interface RegistroService {

    List<Registro> findAll();

    Registro salvarRegistro(Registro registro);

    void validarRegistro(Registro registro);

}
