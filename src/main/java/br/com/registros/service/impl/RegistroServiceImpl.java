package br.com.registros.service.impl;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.repository.RegistroRepository;
import br.com.registros.service.RegistroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroServiceImpl implements RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroServiceImpl(RegistroRepository registroRepository) {

        super();
        this.registroRepository = registroRepository;

    }

    @Override
    @Transactional
    public Registro salvarRegistro(Registro registro) {
        return registroRepository.save(registro);
    }

}
