package br.com.registros.service.impl;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;
import br.com.registros.model.enums.StatusRegistro;
import br.com.registros.model.repository.RegistroRepository;
import br.com.registros.service.RegistroService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RegistroServiceImpl implements RegistroService {

    private static final String TITULO_PADRAO = "Visita normal";

    private final RegistroRepository registroRepository;

    public RegistroServiceImpl(RegistroRepository registroRepository) {

        super();
        this.registroRepository = registroRepository;

    }

    @Override
    public List<Registro> listarRegistrosPorDataDecrescente() {
        return registroRepository.listarRegistrosPorDataDecrescente();
    }

    @Override
    public void validarRegistro(Registro registro) {
        if (StringUtils.isBlank(registro.getTitulo())) {
            registro.setTitulo(TITULO_PADRAO);
        }
    }

    @Override
    public List<TotalRegistros> contarRegistrosPorStatus() {
        return registroRepository.contarRegistrosPorStatus();
    }

    @Override
    @Transactional
    public Registro salvarRegistro(Registro registro) {

        validarRegistro(registro);
        registro.setDataCadastro(LocalDateTime.now());
        Date dataCadastro = toDate(registro.getDataCadastro());
        Optional<Registro> registroEncontrado = Optional.ofNullable(registroRepository.findByStatusAndDataCadastro(registro.getStatus().name(), dataCadastro));

        if (registroEncontrado.isPresent()) {
            String formatoDesejado = "dd/MM/yyyy";
            throw new RegraNegocioException("Já existe um registro para o dia: " + formatarLocalDateTime(registroEncontrado.get().getDataCadastro(), formatoDesejado) + " com o status: " + formatarStatus(registroEncontrado.get().getStatus()));
        }

        return registroRepository.save(registro);
    }

    @Override
    public int processarArquivoCsv(MultipartFile arquivo) throws IOException {

        int quantidade = 0;

        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            while ((linha = bReader.readLine()) != null) {

                String[] registroCsv = linha.split(",+");

                try {

                    StatusRegistro statusRegistro = StatusRegistro.valueOf(registroCsv[1]);
                    LocalDateTime dataHora = LocalDateTime.parse(registroCsv[2]);

                    Date dataCadastro = toDate(dataHora);
                    Optional<Registro> registroEncontrado = Optional.ofNullable(registroRepository.findByStatusAndDataCadastro(statusRegistro.name(), dataCadastro));

                    if (registroEncontrado.isEmpty()) {

                        Registro registro = new Registro();
                        registro.setTitulo(registroCsv[0]);
                        registro.setStatus(statusRegistro);
                        registro.setDataCadastro(dataHora);
                        registro.setLatitude(Double.parseDouble(registroCsv[3]));
                        registro.setLongitude(Double.parseDouble(registroCsv[4]));

                        registroRepository.save(registro);

                        quantidade++;

                    }

                } catch (IllegalArgumentException e) {
                    log.error("Ocorreu um erro na linha do arquivo: " + registroCsv[0] + " - " + e.getMessage());
                }

            }
        } catch (IOException e) {
            log.error("Ocorreu um erro ao ler o arquivo: " + e.getMessage());
        }

        return quantidade;

    }

    private static Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    private static String formatarLocalDateTime(LocalDateTime localDateTime, String formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return localDateTime.format(formatter);
    }

    private static String formatarStatus(StatusRegistro status) {

        String textoStatus = "";

        textoStatus = switch (status) {
            case DIANA -> "Visita na casa da Diana";
            case LIAM -> "Visita na casa do Liam";
            default -> "Passeio";
        };

        return textoStatus;

    }

}
