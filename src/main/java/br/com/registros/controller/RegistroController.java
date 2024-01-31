package br.com.registros.controller;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.entity.Registro;
import br.com.registros.model.dto.RegistroDTO;
import br.com.registros.service.RegistroService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

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

        try {

            Registro registroSalvo = registroService.salvarRegistro(registro);

            return "redirect:/registros/sucesso";

        } catch (RegraNegocioException e) {

            return "redirect:/registros/erro";

        }

    }

}
