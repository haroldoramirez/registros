package br.com.registros.controller;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.dto.RegistroDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @GetMapping("/criar")
    public String paginaCriar() {
        return "registros/criar";
    }

    @GetMapping("/sucesso")
    public String paginaSucesso() {
        return "registros/sucesso";
    }

    @PostMapping("/criar")
    public String criar(RegistroDTO registroDTO) {

        final Registro registro = registroDTO.converterParaRegistro();

        //Chamar service
        //Registro usuarioCriado = criarUsuarioCasoDeUso.criar(registro);

        return "redirect:/registro/sucesso";

    }

}
