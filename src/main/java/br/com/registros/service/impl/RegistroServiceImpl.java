package br.com.registros.service.impl;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;
import br.com.registros.model.enums.StatusRegistro;
import br.com.registros.model.repository.RegistroRepository;
import br.com.registros.service.RegistroService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class RegistroServiceImpl implements RegistroService {

    private static final String TITULO_PADRAO = "Visita normal";

    private final RegistroRepository registroRepository;

    public RegistroServiceImpl(RegistroRepository registroRepository) {

        super();
        this.registroRepository = registroRepository;

    }

    @Override
    public List<Registro> findAll() {
        return registroRepository.findAll();
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
        Registro registroEncontrado = registroRepository.findByStatusAndDataCadastro(registro.getStatus().name(), dataCadastro);

        if (registroEncontrado != null) {
            String formatoDesejado = "dd/MM/yyyy";
            throw new RegraNegocioException("Já existe um registro para o dia: " + formatarLocalDateTime(registroEncontrado.getDataCadastro(), formatoDesejado) + " com o status: " + formatarStatus(registroEncontrado.getStatus()));
        }

        return registroRepository.save(registro);
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
