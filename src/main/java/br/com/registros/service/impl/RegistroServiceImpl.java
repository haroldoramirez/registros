package br.com.registros.service.impl;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.repository.RegistroRepository;
import br.com.registros.service.RegistroService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void validarRegistro(Registro registro) {
        if (StringUtils.isBlank(registro.getTitulo())) {
            registro.setTitulo(TITULO_PADRAO);
        }
    }

    @Override
    @Transactional
    public Registro salvarRegistro(Registro registro) {
        validarRegistro(registro);

        List<Registro> registroEncontrado = registroRepository.findByStatus(registro.getStatus().name());

        return registroRepository.save(registro);
    }

}
