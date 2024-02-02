package br.com.registros.controller;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.dto.RegistroDTO;
import br.com.registros.model.dto.RegistroListDTO;
import br.com.registros.model.entity.Registro;
import br.com.registros.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    private final RegistroService registroService;

    String formatoDesejado = "dd/MM/yyyy HH:mm:ss";

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/criar")
    public String paginaCriar() {
        return "registros/criar";
    }

    @GetMapping("/listar")
    public ModelAndView paginaListar() {
        ModelAndView mv = new ModelAndView("registros/listar");
        List<Registro> registros = registroService.findAll();
        List<RegistroListDTO> registrosDTO = new ArrayList<>();
        String textoStatus = "";

        for (Registro registro : registros) {
            RegistroListDTO registroDTO = new RegistroListDTO();

            registroDTO.setTitulo(registro.getTitulo());
            registroDTO.setDataCadastro(formatarLocalDateTime(registro.getDataCadastro(), formatoDesejado));

            textoStatus = switch (registro.getStatus()) {
                case DIANA -> "Visita na casa da Diana";
                case LIAM -> "Visita na casa do Liam";
                default -> "Passeio";
            };

            registroDTO.setStatus(textoStatus);

            registrosDTO.add(registroDTO);
        }

        mv.addObject("registros", registrosDTO);
        return mv;
    }

    @GetMapping("/sucesso")
    public String paginaSucesso() {
        return "registros/sucesso";
    }

    @GetMapping("/erro")
    public String paginaErro() {
        return "registros/erro";
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

    private static String formatarLocalDateTime(LocalDateTime localDateTime, String formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return localDateTime.format(formatter);
    }

}
