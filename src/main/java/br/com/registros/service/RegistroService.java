package br.com.registros.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;

public interface RegistroService {

    Registro salvarRegistro(Registro registro);

    void validarRegistro(Registro registro);

    List<TotalRegistros> contarRegistrosPorStatus();

    List<Registro> listarRegistrosPorDataDecrescente();

    int processarArquivoCsv(MultipartFile file) throws IOException;

}
