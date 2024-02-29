package br.com.registros.service;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;
import br.com.registros.model.enums.StatusRegistro;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistroService {

    Registro salvarRegistro(Registro registro);

    void validarRegistro(Registro registro);

    List<TotalRegistros> contarRegistrosPorStatus();

    List<Registro> listarRegistrosPorDataDecrescente();

    int processarArquivoCsv(MultipartFile file) throws IOException;

}
