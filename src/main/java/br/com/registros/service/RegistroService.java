package br.com.registros.service;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;

import java.util.List;

public interface RegistroService {

    Registro salvarRegistro(Registro registro);

    void validarRegistro(Registro registro);

    List<TotalRegistros> contarRegistrosPorStatus();

    List<Registro> listarRegistrosPorDataDecrescente();

}
